import React from 'react'
import Image from './Image'
import { Link } from 'react-router-dom'
import axios from '@/api/axios'
import { useQuery } from '@tanstack/react-query'
import { ArticlesPageResponseType } from '@/types/api'
import { format } from 'timeago.js'
import AuthorHover from './AuthorHover'


const fetchArticles = async () => {
    const res = await axios.get("/articles?featured=true", {
        params: {page: 0, limit: 4}
    });

    return res.data;
}


const FeaturedPosts = () => {

    const { data, error, isPending } = useQuery({
        queryFn: fetchArticles,
        queryKey: ['featured-articles']
    })


    if (isPending) return 'Loading ...'
    if (error) return 'Sorry, something went wront!'
    
    const { articles } : ArticlesPageResponseType = data;
    if (articles.length === 0) return;


  return (
    <div className='mt-8 flex flex-col lg:flex-row gap-5'>
        
        <div className='w-full lg:w-1/2 flex flex-col gap-1'>
            {articles[0]?.img && <Image src={articles[0].img} className='rounded-3xl object-cover' w='895' />}
            <Link to={`/${articles[0].slug}`} className='text-xl lg:text-3xl font-semibold lg:font-bold'>1. {articles[0].title}</Link>
            <div className='flex items-center gap-1 text-gray-400 text-sm'>
                <span>Written by</span>
                <AuthorHover author={articles[0].author}>
                  <Link to='/test' className='text-blue-800'>{`${articles[0].author.firstName} ${articles[0].author.lastName}`}</Link>
                </AuthorHover>
                <span>on</span>
                <Link to={`/posts?cat=${articles[0].category.replace(" ", "-")}`} className='text-blue-800 lg:text-lg'>{articles[0].category}</Link>
                <span className='text-gray-500'>{format(articles[0].createdAt)}</span>
            </div>
            <p className='line-clamp-2'>{articles[0].description}</p>
            <Link to={`/${articles[0].slug}`} className='underline text-blue-800 text-sm'>Read More</Link>
        </div>

        <div className='w-full lg:w-1/2 flex flex-col gap-4'>
            {
                articles.map((article, index) => (
                    index > 0 && 
                    <div className='flex justify-between gap-4' key={article.slug}>
                        {article.img && 
                        <div className='w-1/3 aspect-video'>
                            <Image src={article.img} className='rounded-3xl object-cover w-full min-h-32' w='298' h='200' />
                        </div>
                        }
                        <div className='w-2/3'>
                            <Link to={article.slug} className='text-base sm:text-lg md:text-2xl lg:text-lg xl:text-xl font-medium mb-1'>{`${index + 1}. ${article.title}`}</Link>
                            <div className='flex flex-wrap items-center gap-2 text-sm text-gray-400'>
                                <span>Written by</span>
                                <AuthorHover author={article.author}>
                                    <Link to='/test' className='text-blue-800 block'>{`${article.author.firstName} ${article.author.lastName}`}</Link>
                                </AuthorHover>
                                <span className='lg:hidden xl:block'>on</span>
                                <Link to={`/posts?cat=${article.category.replace(" ", "-")}`} className='text-blue-800 lg:hidden xl:block'>{article.category}</Link>
                                <span className='lg:hidden xl:block text-gray-500'>{format(article.createdAt)}</span>
                            </div>
                            <p className='line-clamp-2 mt-3 mb-1'>{article.description}</p>
                            <Link to={`/${article.slug}`} className='underline text-blue-800 text-sm'>Read More</Link> 
                        </div>
                    </div>
                ))
            }
        </div>
    </div>
  )
}

export default FeaturedPosts