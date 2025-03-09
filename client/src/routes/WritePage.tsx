import React, { RefObject, useEffect, useRef, useState } from 'react'
import Upload from '@/components/Upload';
import useAxiosPrivate from '@/hooks/useAxiosPrivate';
import { DraftType, ImageType } from '@/types/model';
import { useMutation, useQuery } from '@tanstack/react-query';
import hljs from 'highlight.js';
import 'highlight.js/styles/base16/edge-light.css'
import Quill, { Delta } from 'quill';
import "quill/dist/quill.snow.css"
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import Image from '@/components/Image';
import { axiosPrivate } from '@/api/axios';

// TODO: FIX SAVED CONTENT BEING OUT OF SYNC WITH ACTUAL CLIENT CONTENT.



const WritePage = () => {
  
    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const { draftId } = useParams();

    const [quill, setQuill] = useState<Quill>()
    const editorRef: RefObject<any> = useRef();
    const unsavedChangesRef = useRef(0);
    
    const [uploadProgress, setUploadProgress] = useState(0)
    const [editorLoaded, setEditorLoaded] = useState(false)
    const [contentChanged, setContentChanged] = useState(false);    

    const [title, setTitle] = useState('');
    const [coverImg, setCoverImg] = useState<ImageType | null>(null)
    const [description, setDescription] = useState('');
    const [category, setCategory] = useState('general');

    
    const {isPending: savingDraft, isSuccess: draftSaved, mutate: saveDraftMutation} = useMutation({
        mutationFn: (payload: DraftType) => {
            return axiosPrivate.put(`/drafts/${draftId}`, payload)
        },
        onSuccess: () => {
            setContentChanged(false)
        },
        onError: () => {
            toast.error("Failed to save changes...")
        }
    })


    // const {isPending: pendingDraftSave, isSuccess: draftSaved, mutate: saveDraft} = useMutation({
    //     mutationFn: (payload: DraftType) => {
    //         console.log(payload.description)
    //         unsavedChangesRef.current = 0;
    //         return axiosPrivate.put(`draft/${draftId}`, payload)
    //     },
    //     onSuccess: () => {
    //         console.log(unsavedChangesRef.current)
    //         if (unsavedChangesRef.current === 0) setContentChanged(false);
    //     },
    //     onError: () => {
    //         toast.error("Failed to save changes...")
    //     }
    // })


    // Load quill
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

            quill.on('text-change', () => {
                setContentChanged(true)
            });
            setQuill(quill)
            
        }

        setEditorLoaded(true)

    }, [quill, editorLoaded])


    useEffect(() => {
        const preventReloadOnUnsavedContent = window.onbeforeunload = () => {
            if (contentChanged) {
                return "There are unsaved changes. Are you sure you want to leave?"
            }
        }

        return () => {
            removeEventListener('beforeunload', preventReloadOnUnsavedContent)
        }
    }, [contentChanged])


    // Load previous changes
    useEffect(() => {
        if (!quill) return;

        const fetchDraft = async () =>  {
            const res = await axiosPrivate.get(`/drafts/${draftId}`);
            const draft:  DraftType = res.data;

            setCoverImg(draft.coverImg)
            setTitle(draft.title)
            setCategory(draft.category.replace(" ", "-"))
            setDescription(draft.description)
            quill.setContents(JSON.parse(draft.content), 'user');
            setContentChanged(false)
        }

        fetchDraft();

    }, [quill])

    const publishMutation = useMutation({
        mutationFn: (draftId: string ) => {
            return axiosPrivate.post(`/drafts/${draftId}/publish`);
        },
        onSuccess: res => {
            toast.success('Article has been published')
            navigate(`/${res.data.slug}`)
        }
    })

    const saveDraft = () => {
        const editContent : DraftType = {
            content: JSON.stringify(quill?.getContents()),
            title: title,
            description: description,
            coverImg: coverImg,
            category: category,
        } 
        
        saveDraftMutation(editContent);
    }


    const handleSubmit = (e) => {
        e.preventDefault();
        if (typeof draftId === 'string') {
            publishMutation.mutate(draftId);
        }
       
    };

    const handleFormChange = (e) => {
        const target = e.target;
        if (target?.accept === "image/*") return;
        setContentChanged(true) 
    }

    
    
  return (
    <div className='h-[calc(100vh-64px)] md:h-[calc(100vh-80px)] flex flex-col gap-6'>
        <div className='flex items-end gap-5'>
            <h1 className='text-xl font-light'>Create a New Post</h1>
            <span className={`text-gray-400 transition-all delay-300`}>
                {savingDraft && 'Saving ...'}
                {draftSaved && !contentChanged && 'Saved'}
            </span>
        </div>
        <form onChange={handleFormChange} onSubmit={handleSubmit} className='flex flex-col gap-6 flex-1 mb-6'>
            <div className='flex items-center gap-10'>
                <Upload type="image" setProgress={setUploadProgress} data={coverImg} setData={setCoverImg} setContentChanged={setContentChanged}>
                    <button name='coverImg' type='button' className='p-2 shadow-md rounded-xl text-sm text-gray-500 bg-white'>Add a cover image</button>
                </Upload>
                {/* {coverImg && <img src={coverImg.url} alt='Article cover image' className='w-20' />} */}
                {coverImg && <Image key={coverImg.name} src={coverImg.filePath} w='200' className='rounded-2xl' />}
                {/* {coverImg && <img src={coverImg.url} className='w-[200px] rounded-2xl' />} */}
            </div>
            
            <input value={title} onChange={(e) => setTitle(e.target.value)} className='text-4xl font-semibold bg-transparent outline-none' type="text" placeholder='A Great Story' name='title' />
            <div className='flex items-center gap-4'>
                <label htmlFor="" className='text-sm'>Choose a category:</label>
                <select value={category} onChange={(e) => setCategory(e.target.value)} name="category" id="" className='p-2 rounded-xl bg-white shadow-md'>
                    <option value="general">General</option>
                    <option value="programming">Programming</option>
                    <option value="Algorithms">Algorithms</option>
                    <option value="data-structures">Data Structures</option>
                    <option value="network-engineering">Network Engineering</option>
                    <option value="spring-boot">Spring Boot</option>
                </select>
            </div>
            <textarea value={description} onChange={(e) => setDescription(e.target.value)} className='p-4 rounded-xl bg-white shadow-md' name="description" placeholder='A Short Description' />
            
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
                {/* <div ref={editorRef} className='flex-1 rounded-xl bg-white shadow-md'></div> */}
            {/* </div> */}

            <div ref={editorRef} className='flex-1 rounded-xl bg-white shadow-md'></div>


            <div className='flex gap-4'>
                <button onClick={saveDraft} type='button' disabled={!contentChanged || (uploadProgress > 0 && uploadProgress < 100)} className='bg-blue-800 text-white font-medium rounded-xl mt-4 p-2 w-36 disabled:bg-blue-400 disabled:cursor-not-allowed'>
                    {savingDraft ? 'Saving' : 'Save'}
                </button>
                <button type='submit' disabled={publishMutation.isPending || (uploadProgress > 0 && uploadProgress < 100) || contentChanged} className='bg-blue-800 text-white font-medium rounded-xl mt-4 p-2 w-36 disabled:bg-blue-400 disabled:cursor-not-allowed'>
                    {publishMutation.isPending ? 'Publishing' : 'Publish'}
                </button>
            </div>


            {'Progress: ' + uploadProgress}
            {publishMutation.isError && <span>{publishMutation.error.message}</span>}


        </form>
    </div>
  )
}

export default WritePage