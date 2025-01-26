import React from 'react'
import Image from './Image'
import { Link } from 'react-router-dom'

const FeaturedPosts = () => {
  return (
    <div className='mt-8 flex flex-col lg:flex-row gap-8'>
        <div className='w-full lg:w-1/2 flex flex-col gap-4'>
            <Image src='/tech-blog/featured1.jpeg' className='rounded-3xl object-cover' w='895' />
            <div className='flex items-center gap-4'>
                <h1 className='font-semibold lg:text-lg'>01.</h1>
                <Link to='' className='text-blue-800 lg:text-lg'>Spring Boot, Java</Link>
                <span className='text-gray-500'>2 days ago</span>
            </div>
            <Link to='/test' className='text-xl lg:text-3xl font-semibold lg:font-bold'>Spring Boot Exception Handling</Link>
        </div>

        <div className='w-full lg:w-1/2 flex flex-col gap-4'>
            <div className='lg:h-1/3 flex justify-between gap-4'>
                <div className='w-1/3 aspect-video'>
                    <Image src='/tech-blog/featured2.jpeg' className='rounded-3xl object-cover w-full h-full' w='298' />
                </div>
                <div className='w-2/3'>
                    <div className='flex items-center gap-4 text-sm lg:text-base mb-4'>
                        <h1 className='font-semibold'>02.</h1>
                        <Link to='' className='text-blue-800'>Programming</Link>
                        <span className='text-gray-500 text-sm'>2 days ago</span>
                    </div>
                    <Link to='/test' className='text-base sm:text-lg md:text-2xl lg:text-xl xl:text-2xl font-medium'>Data Structures and Algorithms</Link>
                </div>
            </div>
            <div className='lg:h-1/3 flex justify-between gap-4'>
                <div className='w-1/3 aspect-video'>
                    <Image src='/tech-blog/featured3.jpeg' className='rounded-3xl object-cover w-full h-full' w='298' />
                </div>
                <div className='w-2/3'>
                    <div className='flex items-center gap-4 text-sm lg:text-base mb-4'>
                        <h1 className='font-semibold'>02.</h1>
                        <Link to='' className='text-blue-800'>Programming, Java</Link>
                        <span className='text-gray-500 text-sm'>2 days ago</span>
                    </div>
                    <Link to='/test' className='text-base sm:text-lg md:text-2xl lg:text-xl xl:text-2xl font-medium'>Data Structures and Algorithms</Link>
                </div> 
            </div>
            <div className='lg:h-1/3 flex justify-between gap-4'>
                <div className='w-1/3 aspect-video'>
                    <Image src='/tech-blog/featured4.jpeg' className='rounded-3xl object-cover w-full h-full' w='298' />
                </div>
                <div className='w-2/3'>
                    <div className='flex items-center gap-4 text-sm lg:text-base mb-4'>
                        <h1 className='font-semibold'>02.</h1>
                        <Link to='' className='text-blue-800'>Programming</Link>
                        <span className='text-gray-500 text-sm'>2 days ago</span>
                    </div>
                    <Link to='/test' className='text-base sm:text-lg md:text-2xl lg:text-xl xl:text-2xl font-medium'>Data Structures and Algorithms</Link>
                </div>
            </div>
            <div className='lg:h-1/3 flex justify-between gap-4'></div>
        </div>
    </div>
  )
}

export default FeaturedPosts