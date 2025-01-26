import { IKContext, IKUpload } from 'imagekitio-react'
import React, { useRef } from 'react'
import { toast } from 'react-toastify'
import useAxiosPrivate from '../hooks/useAxiosPrivate'

const Upload = ({children, type, setProgress, setData}) => {

    const axiosPrivate = useAxiosPrivate();
    const coverRef = useRef(null)

    const authenticator = async () => {
        try {
            const response = await axiosPrivate.get('/articles/upload-auth');
    
            if (response.status !== 200) {
                // const errorText = await response.data;
                throw new Error(`Request failed with status ${response.status}`);
            }

            const { signature, expire, token } = response.data;
            return {signature, expire, token}
            
        } catch (error) {
            throw new Error(`Authentication request failed`)
        }
    }

    const onError = (error) => {
        toast.error("Image upload failed!")
    }

    const onSuccess= (res) => {
        console.log(res)
        setData(res)
    }

    const onUploadProgress = (progress) => {
        setProgress(Math.round((progress.loaded / progress.total) * 100))
    }
    
  return (
    <IKContext 
        publicKey={import.meta.env.VITE_IK_PUBLIC_KEY} 
        urlEndpoint={import.meta.env.VITE_IK_URL_ENDPOINT} 
        authenticator={authenticator}
    >
        <IKUpload
            useUniqueFileName
            onError={onError}
            onSuccess={onSuccess}
            onUploadProgress={onUploadProgress}
            className='hidden'
            ref={coverRef}
            accept={`${type}/*`}
        />
        <div className='cursor-pointer' onClick={() => coverRef.current.click()}>{children}</div>
    </IKContext>
  )
}

export default Upload