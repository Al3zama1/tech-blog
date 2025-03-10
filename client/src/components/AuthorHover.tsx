import { AuthorType } from '@/types/model'
import { Avatar, AvatarFallback, AvatarImage } from '@radix-ui/react-avatar'
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@radix-ui/react-hover-card'
import React, { ReactElement } from 'react'
import Image from './Image'
import { Link } from 'react-router-dom'

type Props = {
    author: AuthorType,
    children: ReactElement
}

const AuthorHover = ( {author, children } : Props) => {
  return (
    <HoverCard>
        <HoverCardTrigger>{children}</HoverCardTrigger>
        <HoverCardContent className='bg-white min-w-60 max-w-72 p-4 rounded-md shadow-xl'>
            <Avatar className='inline-block'>
                <Link to={`/authors/${author.id}`}>
                    <AvatarImage src={author.profileImg} className='mb-4' />
                </Link>
                <AvatarFallback>
                    {/* {author.firstName.charAt(0)} */}
                    <Link to={`/authors/${author.id}`}>
                        <Image src='/tech-blog/avatar.jpeg' className='rounded-full w-12' />
                    </Link>
                </AvatarFallback>
            </Avatar>
            <Link to={`/authors/${author.id}`}>
                <h4 className='font-bold text-black mt-2'>{`${author.firstName} ${author.lastName}`}</h4>
            </Link>
            <p className='text-black mt-4 text-sm'>{author.bio}</p>
        </HoverCardContent>
    </HoverCard>
  )
}

export default AuthorHover