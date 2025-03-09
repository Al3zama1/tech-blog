import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import MainCategories from '../components/MainCategories'
import FeaturedPosts from '../components/FeaturedPosts'
import PostList from '../components/PostList'
import { useMutation } from '@tanstack/react-query'
import useAxiosPrivate from '@/hooks/useAxiosPrivate'
import useAuth from '@/hooks/UseAuth'

const HomePage = () => {

    const navigate = useNavigate();
    const axiosPrivate = useAxiosPrivate();
    const { user } = useAuth();

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    console.log("home page loaded")


    const createDraft = useMutation({
        mutationFn: () => {
            return axiosPrivate.post('/drafts');
        },
        onSuccess: res => {
            const draftId = res.headers?.location.split("/").pop();
            navigate(`/draft/${draftId}`)
        }
    })

    const canCreateDraft = () => {
        return user?.roles.includes("ADMIN") || user?.roles.includes("EDITOR")
    }

  return (
    <div className='mt-4 flex flex-col gap-4'>
        {/* Breadcrum */}
        {/* <div className='flex gap-4'>
            <Link to='/'>Home</Link>
            <span>.</span>
            <span className='text-blue-800'>Blogs and Articles</span>
        </div> */}
        {/* Introduction */}
        <div className='flex items-center justify-between'>
            <div className=''>
                <h1 className='text-gray-800 text-2xl md:text-5xl lg:text-6xl font-bold'>Unlock Your Tech Potential - Learn Technology Step by Step.</h1>
                <p className='mt-8 text-md md:text-xl'>Articles focused on programming, network engineering, information technology, and personal projects.</p>
            </div>
            {/* {canCreateDraft() &&
            <div onClick={() => createDraft.mutate()} className='hidden md:block relative'>
                <svg
                    viewBox="0 0 200 200"
                    width="200"
                    height="200"
                // className="text-lg tracking-widest"
                    className="text-lg tracking-widest animate-spin animatedButton"
                >
                    <path
                        id="circlePath"
                        fill="none"
                        d="M 100, 100 m -75, 0 a 75,75 0 1,1 150,0 a 75,75 0 1,1 -150,0"
                    />
                    <text>
                        <textPath href="#circlePath" startOffset="0%">
                            Write your story •
                        </textPath>
                        <textPath href="#circlePath" startOffset="50%">
                            Share your idea •
                        </textPath>
                    </text>
                </svg>
                <button className="absolute top-0 left-0 right-0 bottom-0 m-auto w-20 h-20 bg-blue-800 rounded-full flex items-center justify-center">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        width="50"
                        height="50"
                        fill="none"
                        stroke="white"
                        strokeWidth="2"
                    >
                        <line x1="6" y1="18" x2="18" y2="6" />
                        <polyline points="9 6 18 6 18 15" />
                    </svg>
                </button>
            </div>
            } */}
        </div>
        {/* <MainCategories /> */}
        {/* Featured Posts */}
        <FeaturedPosts />
        {/* Categories */}
        {/* Post List */}
        <div>
            <h1 className='my-8 text-2xl text-gray-600'>Recent Articles</h1>
            <PostList />
        </div>
    </div>
  )
}

export default HomePage