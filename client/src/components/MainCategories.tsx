import React from 'react'
import { Link } from 'react-router-dom'

const MainCategories = () => {
  return (
    <div className='hidden md:flex bg-white rounded-3xl xl:rounded-full p-4 shadow-lg items-center justify-center gap-8'>
        {/* links */}
        {/* flex-1 flex items-center justify-between flex-wrap */}
        <div className='flex flex-1 justify-between items-center flex-wrap gap-1'>
            <Link to='/posts' className='bg-blue-800 text-white rounded-full px-4 py-2'>All Posts</Link>
            <Link to='/posts?cat=network-engineering' className='hover:bg-blue-50 rounded-full px-4 py-2'>Network Engineering</Link>
            <Link to='/posts?cat=spring-boot' className='hover:bg-blue-50 rounded-full px-4 py-2'>Spring Boot</Link>
            <Link to='/posts?cat=java' className='hover:bg-blue-50 rounded-full px-4 py-2'>Java</Link>
            <Link to='/posts?cat=data-structures' className='hover:bg-blue-50 rounded-full px-4 py-2'>Data Structures</Link>
            <Link to='/posts?cat=algorithms' className='hover:bg-blue-50 rounded-full px-4 py-2'>Algorithms</Link>
        </div>
        <span className='text-xl font-medium'>|</span>
        <div className='bg-gray-100 p-2 rounded-full flex items-center gap-2'>
            <svg
                xmlns='http://www.w3.org/2000/svg'
                viewBox='0 0 24 24'
                width='20'
                height='20'
                fill='none'
                stroke='gray'
            >
                <circle cx='10.5' cy='10.5' r='7.5' />
                <line x1='16.5' y1='16.5' x2='22' y2='22' />
            </svg>
            <input type="text" placeholder='search a post...' className='bg-transparent' />
        </div>
    </div>
  )
}

export default MainCategories