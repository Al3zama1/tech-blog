import React, { useState } from 'react'
import { Link } from 'react-router-dom'


type ActiveCagegory = {
    category: "all" | 'network-engineering' | 'spring-boot' | 'java' | 'data-structures' | 'algorithms'
}

const MainCategories = () => {

    const [active, setActive] = useState("")

    const handleCategoryClick = (category: string) => {
    }


  return (
    <div className='hidden md:flex bg-white rounded-3xl xl:rounded-full p-4 shadow-lg mt-5 mb-3'>
        {/* links */}
        {/* flex-1 flex items-center justify-between flex-wrap */}
        <div className='flex flex-1 items-center flex-wrap gap-4'>
            <button className={`rounded-full px-4 py-2 ${active === '' ? 'bg-blue-800 text-white' : 'hover:bg-blue-50'}`} onClick={() => handleCategoryClick("")}>All Posts</button>
            {/* <Link to='/posts' className='bg-blue-800 text-white rounded-full px-4 py-2'>All Posts</Link> */}
            <button className={`rounded-full px-4 py-2 ${active === 'network-engineering' ? 'bg-blue-800 text-white' : 'hover:bg-blue-50 '}`} onClick={() => handleCategoryClick("network-engineering")}>Network Engineering</button>
            <button className={`rounded-full px-4 py-2 ${active === 'spring-boot' ? 'bg-blue-800 text-white' : 'hover:bg-blue-50 '}`} onClick={() => handleCategoryClick("spring-boot")}>Spring Boot</button>
            <button className={`rounded-full px-4 py-2 ${active === 'java' ? 'bg-blue-800 text-white' : 'hover:bg-blue-50 '}`} onClick={() => handleCategoryClick("java")}>Java</button>
            <button className={`rounded-full px-4 py-2 ${active === 'data-structures' ? 'bg-blue-800 text-white' : 'hover:bg-blue-50 '}`} onClick={() => handleCategoryClick("data-structures")}>Data Structures</button>
            <button className={`rounded-full px-4 py-2 ${active === 'algorithms' ? 'bg-blue-800 text-white hover:bg-none' : 'hover:bg-blue-50 '}`} onClick={() => handleCategoryClick("algorithms")}>Algorithms</button>
        </div>
        {/* <span className='text-xl font-medium'>|</span> */}
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
            <input type="text" placeholder='search a post...' className='bg-transparent focus:outline-none' />
        </div>
    </div>
  )
}

export default MainCategories