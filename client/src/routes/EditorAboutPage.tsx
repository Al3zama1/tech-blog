import React from 'react'
import { useParams } from 'react-router-dom'

const EditorAboutPage = () => {
    const params = useParams();

    console.log(params.authorId)
  return (
    <div className='flex'>
        <div>
            
        </div>
        <div>

        </div>
    </div>
  )
}

export default EditorAboutPage