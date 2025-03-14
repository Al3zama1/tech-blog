import React from 'react'
import Search from './Search'
import { Link } from 'react-router-dom'

const SideMenu = () => {
  return (
    <div className='px-4 h-max sticky top-8'>
        <h1 className='mb-4 text-sm font-medium'>Search</h1>
        <Search />
        <h1 className='mt-8 mb-4 text-sm font-medium'>Filter</h1>
        <div className='flex flex-col gap-2 text-sm'>
            <label htmlFor="" className='flex items-center gap-2 cursor-pointer'>
                <input type="radio" name='sort' value='newest' className='appearance-none w-4 h-4 border-[1.5px] border-blue-800 bg-white cursor-pointer rounded-sm checked:bg-blue-800' />
                Newest
            </label>
            <label htmlFor="" className='flex items-center gap-2 cursor-pointer'>
                <input type="radio" name='sort' value='popular' className='appearance-none w-4 h-4 border-[1.5px] border-blue-800 bg-white cursor-pointer rounded-sm checked:bg-blue-800' />
                Most Popular
            </label>
            <label htmlFor="" className='flex items-center gap-2 cursor-pointer'>
                <input type="radio" name='sort' value='trending' className='appearance-none w-4 h-4 border-[1.5px] border-blue-800 bg-white cursor-pointer rounded-sm checked:bg-blue-800' />
                Trending
            </label>
        </div>
        <h1 className='mt-8 mb-4 text-sm font-medium'>Categories</h1>
        <div className='flex flex-col gap-2 text-sm'>
            <Link className='underline' to='/posts'>All</Link>
            <Link className='underline' to='/posts?cat=programming'>Programming</Link>
            <Link className='underline' to='/posts?cat=algorithms'>Algorithms</Link>
            <Link className='underline' to='/posts?cat=network-engineering'>Network Engineering</Link>
            <Link className='underline' to='/posts?cat=data-structures'>Data Structures</Link>
        </div>
    </div>
  )
}

export default SideMenu