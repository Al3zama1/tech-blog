import React from 'react'
import PostListItem from './PostListItem'
import { useQuery } from '@tanstack/react-query'
import { axiosPrivate } from '../api/axios'

const fetchPosts = async () => {
  const res = await axiosPrivate.get('/articles');
  return res.data;
}

const PostList = () => {

  const {isPending, error, data} = useQuery({
    queryKey: ['repoData'],
    queryFn: () => fetchPosts(),
  })

  if (isPending) return 'Loading...';
  if (error) return 'An error has occurred: ' + error.message;

  return (
    <div className='flex flex-col gap-12 mb-8'>
        <PostListItem />
        <PostListItem />
        <PostListItem />
        <PostListItem />
        <PostListItem />
    </div>
  )
}

export default PostList