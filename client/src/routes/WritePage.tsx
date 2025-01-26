import React, { RefObject, useEffect, useRef, useState } from 'react'
import "quill/dist/quill.snow.css"
import hljs from 'highlight.js'

import "highlight.js/styles/base16/gruvbox-dark-pale.css"
// import 'highlight.js/styles/base16/harmonic16-light.css'
// import 'highlight.js/styles/base16/papercolor-light.css'

// import 'highlight.js/styles/qtcreator-light.css'

// import 'highlight.js/styles/ascetic.css'
// import 'highlight.js/styles/vs.css'

// import 'highlight.js/styles/github.css'

// import 'highlight.js/styles/base16/github.css'
// import 'highlight.js/styles/base16/grayscale-light.css'
// import 'highlight.js/styles/base16/papercolor-dark.css'
// import 'highlight.js/styles/base16/edge-light.css'
// import 'highlight.js/styles/arduino-light.min.css'
// import 'highlight.js/styles/atom-one-dark-reasonable.min.css'

import useAuth from '../hooks/UseAuth'
import Quill from 'quill';
import { useMutation } from '@tanstack/react-query'
import useAxiosPrivate from '../hooks/useAxiosPrivate'
import { data, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { IKContext, IKUpload } from 'imagekitio-react'
import Upload from '../components/Upload'


const WritePage = () => {

    const {isLoaded, accessToken} = useAuth();
    const axiosPrivate = useAxiosPrivate();

    const navigate = useNavigate();

    const [quill, setQuill] = useState<Quill>()
    const [coverImg, setCoverImg] = useState('')
    const [uploadProgress, setUploadProgress] = useState(0)
    const [editorLoaded, setEditorLoaded] = useState(false)
    const editorRef: RefObject<any> = useRef();

    useEffect(() => {
        if (editorRef && !quill && editorLoaded) {
            const quill = new Quill(editorRef.current, {
                placeholder: 'Tell your story...',
                modules: {
                    syntax: {hljs},
                    toolbar: '#toolbar'
                },
                theme: 'snow'
            })
            setQuill(quill)
        }

        setEditorLoaded(true)

    }, [quill, editorLoaded])

    const mutation = useMutation({
        mutationFn: newArticle => {
            return axiosPrivate.post('/articles', newArticle);
        },
        onSuccess: res => {
            toast.success('Article has been published')
            navigate(`/${res.data.slug}`)
        }
    })

    // if (!isLoaded) return <div>Loading...</div>

    // if (isLoaded && !accessToken) return <div>You should login!</div>

    const handleSubmit = (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const data = {
            title: formData.get('title'),
            img: coverImg.filePath,
            category: formData.get('category'),
            description: formData.get('description'),
            content: quill?.getContents()
        }

        console.log(data)
        mutation.mutate(data);
    };

    
    
  return (
    <div className='h-[calc(100vh-64px)] md:h-[calc(100vh-80px)] flex flex-col gap-6'>
        <h1 className='text-xl font-light'>Create a New Post</h1>
        <form onSubmit={handleSubmit} className='flex flex-col gap-6 flex-1 mb-6'>
            <Upload type="image" setProgress={setUploadProgress} setData={setCoverImg}>
                <button className='w-max p-2 shadow-md rounded-xl text-sm text-gray-500 bg-white'>Add a cover image</button>
            </Upload>
            <input className='text-4xl font-semibold bg-transparent outline-none' type="text" placeholder='A Great Story' name='title' />
            <div className='flex items-center gap-4'>
                <label htmlFor="" className='text-sm'>Choose a category:</label>
                <select name="category" id="" className='p-2 rounded-xl bg-white shadow-md'>
                    <option value="general">General</option>
                    <option value="programming">Programming</option>
                    <option value="Algorithms">Algorithms</option>
                    <option value="data-structures">Data Structures</option>
                    <option value="network-engineering">Network Engineering</option>
                    <option value="spring-boot">Spring Boot</option>
                </select>
            </div>
            <textarea className='p-4 rounded-xl bg-white shadow-md' name="description" placeholder='A Short Description' />
            {/* <ReactQuill theme="snow" modules={{syntax: hljs, }} className='flex-1 rounded-xl bg-white shadow-md' /> */}
            <div id='toolbar'>
                <span className="ql-formats">
                    <button className="ql-header" value='1'></button>
                    <button className="ql-header" value='2'></button>
                    <button className="ql-header" value='3'></button>
                </span>
                <span className="ql-formats">
                    <button className="ql-bold"></button>
                    <button className="ql-italic"></button>
                    <button className="ql-underline"></button>
                    <button className="ql-strike"></button>
                    <button className="ql-link"></button>
                </span>
                <span className="ql-formats">
                    <button className="ql-list" value='ordered'></button>
                    <button className="ql-list" value='bullet'></button>
                </span>
                <span className="ql-formats">
                    <button className="ql-code"></button>
                    <button className="ql-code-block"></button>
                </span>
                <span className="ql-formats">
                    <button className="ql-image"></button>
                </span>
            </div>
            {/* <div className='flex flex-1'>
                <div className='flex flex-col gap-2 mr-2'>
                    <div>🌄</div>
                    <div>▶️</div>
                </div> */}
                <div ref={editorRef} className='flex-1 rounded-xl bg-white shadow-md'></div>
            {/* </div> */}
            <button disabled={mutation.isPending || (uploadProgress > 0 && uploadProgress < 100)} className='bg-blue-800 text-white font-medium rounded-xl mt-4 p-2 w-36 disabled:bg-blue-400 disabled:cursor-not-allowed'>
                {mutation.isPending ? 'Publishing' : 'Publish'}
            </button>
            {'Progress: ' + uploadProgress}
            {mutation.isError && <span>{mutation.error.message}</span>}

        </form>
    </div>
  )
}

export default WritePage