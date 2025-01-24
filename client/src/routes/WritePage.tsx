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

const WritePage = () => {

    const {isLoaded, accessToken} = useAuth();

    const [quill, setQuill] = useState<Quill>()
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

    // if (!isLoaded) return <div>Loading...</div>

    // if (isLoaded && !accessToken) return <div>You should login!</div>
    
  return (
    <div className='h-[calc(100vh-64px)] md:h-[calc(100vh-80px)] flex flex-col gap-6'>
        <h1 className='text-xl font-light'>Create a New Post</h1>
        <form className='flex flex-col gap-6 flex-1 mb-6'>
            <button className='w-max p-2 shadow-md rounded-xl text-sm text-gray-500 bg-white'>Add a cover image</button>
            <input className='text-4xl font-semibold bg-transparent outline-none' type="text" placeholder='A Great Story' />
            <div className='flex items-center gap-4'>
                <label htmlFor="" className='text-sm'>Choose a category:</label>
                <select name="cat" id="" className='p-2 rounded-xl bg-white shadow-md'>
                    <option value="general">General</option>
                    <option value="programming">Programming</option>
                    <option value="Algorithms">Algorithms</option>
                    <option value="data-structures">Data Structures</option>
                    <option value="network-engineering">Network Engineering</option>
                    <option value="spring-boot">Spring Boot</option>
                </select>
            </div>
            <textarea className='p-4 rounded-xl bg-white shadow-md' name="desc" placeholder='A Short Description'></textarea>
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
            <div ref={editorRef} className='flex-1 rounded-xl bg-white shadow-md'></div>
            <button className='bg-blue-800 text-white font-medium rounded-xl mt-4 p-2 w-36'>Send</button>
        </form>
    </div>
  )
}

export default WritePage