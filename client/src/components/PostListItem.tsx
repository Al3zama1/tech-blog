import React from 'react'
import Image from './Image'
import { Link } from 'react-router-dom'

const PostListItem = () => {
  return (
    <div className='flex flex-col xl:flex-row gap-8'>
        <div className='md:hidden xl:block xl:w-1/3'>
            <Image src='/tech-blog/postImg.jpeg' className='rounded-2xl object-cover' w='735' />
        </div>
        <div className='flex flex-col gap-4 xl:w-2/3'>
            <Link to='/test' className='text-4xl font-semibold'>Spring Boot Unit Testing</Link>
            <div className='flex items-center gap-2 text-gray-400 text-sm'>
                <span>Written by</span>
                <Link to='/test' className='text-blue-800'>John Doe</Link>
                <span>on</span>
                <Link to='/test' className='text-blue-800'>Programming</Link>
                <span>2 days ago</span>
            </div>
            <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Ab, placeat? Eius laborum dolores qui eligendi autem at perferendis officia rerum vero modi, expedita beatae nam explicabo, ex amet culpa non!</p>
            <Link to='/test' className='underline text-blue-800 text-sm'>Read More</Link>
        </div>
    </div>
  )
}

export default PostListItem