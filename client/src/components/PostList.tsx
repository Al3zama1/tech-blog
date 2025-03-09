import React, { useState } from 'react'
import PostListItem from './PostListItem'
import { useInfiniteQuery, useQuery } from '@tanstack/react-query'
import axios, { axiosPrivate } from '../api/axios'
import InfiniteScroll from "react-infinite-scroll-component";
import { ArticlePreviewType } from '@/types/model';
import { useSearchParams } from 'react-router-dom';
import { randomUUID } from 'crypto';





const fetchPosts = async (pageParam : number, searchParams) => {
  const searchParamsObj = Object.fromEntries([...searchParams]);

  const res = await axios.get("/articles", {
    params: {page: pageParam, limit: 10, ...searchParamsObj}
  });

  return res.data;
}


const PostList = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const {
    data, 
    error,
    fetchNextPage, 
    hasNextPage,
    isFetching,
    isFetchingNextPage
  } = useInfiniteQuery({
    queryKey: ['posts', searchParams.toString()],
    queryFn: ({ pageParam = 0 }) => fetchPosts(pageParam, searchParams),
    initialPageParam: 0,
    getNextPageParam: (lastPage, pages) => {
      return lastPage.hasMorePages ? pages.length : undefined
    }
  })

  if (isFetching) return 'Loading';
  if (error) return 'Something went wrong!';


  const allPosts : ArticlePreviewType[] = data?.pages?.flatMap((page) => page.articles) || [];
  
  return (
    <InfiniteScroll
      dataLength={allPosts.length}
      next={fetchNextPage}
      hasMore={!!hasNextPage}
      loader={<h4>Loading more posts...</h4>}
      endMessage={
        <p>
          <b>All posts loaded!</b>
        </p>
      }
    >
      {allPosts.map(post => (
        <PostListItem key={post.slug} post={post} />
      ))}
    </InfiniteScroll>
  )
}

export default PostList