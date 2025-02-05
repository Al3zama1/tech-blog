import { useState } from "react"

import { IKImage } from 'imagekitio-react';
import Image from "./Image";
import { Link } from "react-router-dom";
import useAuth from '../hooks/UseAuth'
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";

const Navbar = () => {

    const [open, setOpen] = useState(false)
    const { user } = useAuth();


  return (
    <div className="w-full h-16 md:h-20 flex items-center justify-between">
        {/* Logo */}
        <Link to='/' className="flex items-center gap-4 text-2xl font-bold">
            {/* <Image src="tech-blog/tech-blog-logo.svg" alt="Blog logo" w='32' h='32' /> */}
            <img src="./logo.png" className='w-52' />
        </Link>
        {/* Mobile Menu */}
        <div className='md:hidden'>
            <div className="cursor-pointer text-4xl" onClick={() => setOpen(prev => !prev)}>
                {open ? 'X' : '☰'}
            </div>
            <div className={`w-full h-screen flex flex-col items-center justify-center gap-8 font-medium text-lg absolute top-16 bg-[#e6e6ff] transition-all ease-in-out ${open ? "-right-0" : "-right-[100%]"}`}>
                <Link to='/'>Home</Link>
                <Link to='/'>Trending</Link>
                <Link to='/'>Most Popular</Link>
                <Link to='/'>About</Link>
                {user === null ? 
                <Link to='/login'>
                    <button className="py-2 px-4 rounded-3xl bg-blue-800 text-white">Login 👋</button>
                </Link>
                :
                <Avatar>
                    <AvatarImage src={user.profileImg} />
                    <AvatarFallback className="shadow-md p-2 rounded-full hover:cursor-pointer">{user.firstName.charAt(0) + user.lastName.charAt(0)}</AvatarFallback>
                </Avatar>
                }
                {/* <Link to='/'>
                    <button className="py-2 px-4 rounded-3xl bg-blue-800 text-white">Login 👋</button>
                </Link> */}
            </div>
        </div>

        {/* Desktop Menu */}
        <div className='hidden md:flex items-center gap-8 xl:gap-12 font-medium'>
            <Link to='/'>Home</Link>
            <Link to='/'>Trending</Link>
            <Link to='/'>Most Popular</Link>
            <Link to='/'>About</Link>
            {user === null ? 
            <Link to='/login'>
                <button className="py-2 px-4 rounded-3xl bg-blue-800 text-white">Login 👋</button>
            </Link>
            :
            <Avatar>
                <AvatarImage src={user.profileImg} />
                <AvatarFallback className="shadow-md p-2 rounded-full hover:cursor-pointer">{user.firstName.charAt(0) + user.lastName.charAt(0)}</AvatarFallback>
            </Avatar>
            }
            
        </div>
    </div>
  )
}

export default Navbar