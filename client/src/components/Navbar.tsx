import { useState } from "react"

import { IKImage } from 'imagekitio-react';
import Image from "./Image";
import { Link } from "react-router-dom";
import useAuth from '../hooks/UseAuth'
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";
import { DropdownMenu, DropdownMenuContent, DropdownMenuLabel, DropdownMenuTrigger } from "@radix-ui/react-dropdown-menu";
import UserDropDownMenu from "./UserDropDownMenu";
import { SquarePen } from "lucide-react";
// import { NavigationMenu, NavigationMenuList } from "@radix-ui/react-navigation-menu";
import { NavigationMenuItem, NavigationMenu, NavigationMenuList, NavigationMenuTrigger, NavigationMenuContent, NavigationMenuLink } from "./ui/navigation-menu";
import React from "react";
import { cn } from "@/lib/utils";

const components: { title: string; href: string; description: string }[] = [
    {
      title: "Network Engineering",
      href: "/posts/?cat=network-engineering",
      description:
        "A modal dialog that interrupts the user with important content and expects a response.",
    },
    {
      title: "Programming",
      href: "/posts/?cat=programming",
      description:
        "For sighted users to preview content available behind a link.",
    },
    {
      title: "Information Technology",
      href: "/posts/?cat=information-technology",
      description:
        "Displays an indicator showing the completion progress of a task, typically displayed as a progress bar.",
    }
  ]
  

const Navbar = () => {

    const [open, setOpen] = useState(false)
    const { user } = useAuth();

    const canWrite = () => {
        return user?.roles.includes("ADMIN") || user?.roles.includes("EDITOR")
    }


  return (
    <div className="w-full h-16 md:h-20 flex items-center justify-between">
        {/* Logo */}
        <nav>
            <ul className="flex gap-4 items-center">
                <li>
                    <Link to='/' className="flex items-center gap-4 text-2xl font-bold">
                        <Image src="tech-blog/logo.png" className="w-52" />
                    </Link>
                </li>
                {/* <li className="bg-gray-100 p-2 rounded-full hidden lg:flex items-center gap-2">
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
                </li> */}
            </ul>
        </nav>
        {/* Mobile Menu */}
        <nav className='md:hidden'>
            <div className="cursor-pointer text-4xl" onClick={() => setOpen(prev => !prev)}>
                {open ? 'X' : '☰'}
            </div>
            <ul className={`w-full h-screen flex flex-col items-center justify-center gap-8 font-medium text-lg absolute top-16 bg-white transition-all ease-in-out ${open ? "-right-0" : "-right-[100%]"}`}>
                <li>
                    <Link to='/' className="hover:bg-gray-100 py-2 px-4 rounded-md">Home</Link>
                </li>
                <li>
                    <Link to='/' className="hover:bg-gray-100 py-2 px-4 rounded-md">Trending</Link>
                </li>
                <li>
                    <Link to='/' className="hover:bg-gray-100 py-2 px-4 rounded-md">Most Popular</Link>
                </li>
                <li>
                    <Link to='/' className="hover:bg-gray-100 py-2 px-4 rounded-md">About</Link>
                </li>
                {user === null ? 
                <li>
                    <Link to='/login'>
                        <button className="py-2 px-4 rounded-3xl bg-blue-800 text-white">Login 👋</button>
                    </Link>
                </li>
                :
                <li>
                    <Avatar>
                        <AvatarImage src={user.profileImg} />
                        <AvatarFallback className="shadow-md p-2 rounded-full hover:cursor-pointer">{user.firstName.charAt(0) + user.lastName.charAt(0)}</AvatarFallback>
                    </Avatar>
                </li>
                }
                {/* <Link to='/'>
                    <button className="py-2 px-4 rounded-3xl bg-blue-800 text-white">Login 👋</button>
                </Link> */}
            </ul>
        </nav>

        {/* Desktop Menu */}
        <nav className="hidden md:block">
            <ul className="hidden md:flex items-center gap-1 font-medium">
                <li>
                    <Link to='/' className="hover:bg-gray-100 py-2 px-4 rounded-md">Home</Link>
                </li>
                <CategoryDropdownMenu />
                <li>
                    <Link to='/' className="hover:bg-gray-100 py-2 px-4 rounded-md">About</Link>
                </li>
                {canWrite() && 
                <li>
                    <Link to='/' className="flex items-center gap-2 px-4 py-2 hover:bg-gray-100 rounded-md">
                        <SquarePen className="size-5" /> <span>Write</span>
                    </Link>
                </li>
                }
                {user === null ? 
                <li>
                    <Link to='/login'>
                        <button className="py-2 px-4 rounded-3xl bg-blue-800 text-white">Login 👋</button>
                    </Link>
                </li>
                :
                <UserDropDownMenu user={user} />
                }
            </ul>
        </nav>
    </div>
  )
}

const CategoryDropdownMenu = () => {

    return (
        <NavigationMenu>
            <NavigationMenuList>
                <NavigationMenuItem className="hover:bg-none">
                    <NavigationMenuTrigger className="text-base">Categories</NavigationMenuTrigger>
                    <NavigationMenuContent>
                        <ul className="px-4 py-2 w-[250px]">
                            {components.map(component => (
                                <li>
                                    <Link to={component.href} className="block my-2 hover:bg-gray-100 px-4 py-2 rounded-md">{component.title}</Link>
                                </li>
                            ))}
                        </ul>
                    </NavigationMenuContent>
                </NavigationMenuItem>
            </NavigationMenuList>
        </NavigationMenu>
    )
}

export default Navbar