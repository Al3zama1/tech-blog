import React from 'react'
import Image from './Image'
import { Link } from 'react-router-dom'
import { ArticlePreviewType } from '@/types/model'
import { format } from 'timeago.js'
import AuthorHover from './AuthorHover'

type Props = {
  post: ArticlePreviewType
}

const PostListItem = ({ post } : Props) => {
  return (
    <div className='flex flex-col xl:flex-row xl:gap-5 gap-2 mb-12'>
        <div className='xl:w-1/3'>
            <Image src={post.img} className='rounded-2xl object-cover ' w='535'   />
        </div>
        <div className='flex flex-col xl:w-2/3'>
            <Link to={`/${post.slug}`} className='text-4xl my-1 font-semibold'>{post.title}</Link>
            <div className='flex items-center gap-2 text-gray-400 text-sm'>
                <span>Written by</span>
                <AuthorHover author={post.author}>
                  <Link to='/test' className='text-blue-800'>{`${post.author.firstName} ${post.author.lastName}`}</Link>
                </AuthorHover>
                <span>on</span>
                <Link to={`/posts?cat=${post.category.replace(" ", "-")}`} className='text-blue-800'>{post.category}</Link>
                <span className='text-gray-500'>{format(post.createdAt)}</span>
            </div>
            <p className='mt-3 mb-1'>{post.description}</p>
            <Link to={`/${post.slug}`} className='underline text-blue-800 text-sm'>Read More</Link>
        </div>
    </div>
  )
}

export default PostListItem