import React, { RefObject, useEffect, useRef, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import Image from '../components/Image'
import PostMenuActions from '../components/PostMenuActions'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faGithub } from '@fortawesome/free-brands-svg-icons/faGithub'
import { faLinkedin } from '@fortawesome/free-brands-svg-icons'
import Search from '../components/Search'
import Comments from '../components/Comments'
import axios from '@/api/axios'
import { useQuery } from '@tanstack/react-query'
import { ArticleType } from '@/types/model'
import Quill from 'quill'
import hljs from 'highlight.js'
import { format } from 'timeago.js'


const fetchPost = async (slug: string) => {
    const res = axios.get(`/articles/${slug}`)
    return (await res).data;
}


const SinglePostPage = () => {

    const { slug } = useParams();
    const articleRef : RefObject<any> = useRef();
    const [articleLoaded, setArticleLoaded] = useState(false)
    const [quill, setQuill] = useState<Quill>();


    const { isPending, error, data, isSuccess } = useQuery({
        queryKey: ["post", slug],
        queryFn: () => {
            if (slug) return fetchPost(slug)
        }
    });

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);


    useEffect(() => {
        if (articleRef.current) {
            const quill = new Quill(articleRef.current, {
                readOnly: true,
                modules: {
                    syntax: {hljs}
                }
            })
            quill.setContents(JSON.parse(data.content))
        }
        setArticleLoaded(true)

    }, [data])


    if (isPending) return "Loading";
    if (error) return "Something went wrong! " + error.message
    if (!data) return "Post not found"

    const article : ArticleType = data;
    
    
  return (
    <div className='flex flex-col gap-8'>
        <div className='flex gap-8'>
            <div className='lg:w-3/5 flex flex-col gap-8'>
                <h1 className='text-xl md:text-3xl xl:text-4xl 2xl:text-5xl font-semibold'>{article.title}</h1>
                <div className='flex items-center gap-2 text-gray-400 text-sm'>
                    <span>Written by </span>
                    <Link to='/test' className='text-blue-800'>{`${article.author.firstName} ${article.author.lastName}`}</Link>
                    <span>on</span>
                    <Link to={`/posts/?cat=${article.category.replace(" ", "-")}`} className='text-blue-800'>{article.category}</Link>
                    <span>{format(article.createdAt)}</span>
                </div>
                <p className='text-gray-500 font-medium'>
                    {article.description}
                </p>
            </div>
            <div className='hidden lg:block w-2/5'>
                {article.coverImg && <Image src={article.coverImg} w='600' className='rounded-2xl' />}
            </div>
        </div>
        <div className='flex justify-between flex-col md:flex-row gap-12'>
            <div ref={articleRef} className='article'></div>
            <div className='px-4 h-max sticky top-8 min-w-60'>
                <h1 className='mb-4 text-sm font-medium'>Author</h1>
                <div className='flex flex-col gap-4'>
                    <div className='flex items-center gap-4'>
                        {article.author.profileImg && <Image src={article.author.profileImg} className='w-12 h-12 rounded-full object-cover' w='48' h='48' /> }
                        <Link to='/test' className='text-blue-800'>{`${article.author.firstName} ${article.author.lastName}`}</Link>
                    </div>
                    <p className='text-sm text-gray-500'>{article.author.introduction}</p>
                    <div className='flex gap-3'>
                        { article.author.linkedInUrl && <Link to={article.author.linkedInUrl} ><FontAwesomeIcon icon={faLinkedin} /></Link> }
                        { article.author.gitHubUrl && <Link to={article.author.gitHubUrl} ><FontAwesomeIcon icon={faGithub} /></Link> }
                    </div>
                </div>
                {slug && <PostMenuActions article={article} slug={slug} /> }
                <h1 className='mt-8 mb-4 text-sm font-medium'>Categories</h1>
                <div className='flex flex-col gap-2 text-sm'>
                    <Link to='/test' className='underline'>Programming</Link>
                    <Link to='/test' className='underline'>Network Engineering</Link>
                    <Link to='/test' className='underline'>Spring Boot</Link>
                    <Link to='/test' className='underline'>All</Link>
                </div>
                <h1 className='mt-8 mb-4 text-sm font-medium'>Search</h1>
                <Search />
            </div>
        </div>
        <Comments />
    </div>
  )
}

export default SinglePostPage