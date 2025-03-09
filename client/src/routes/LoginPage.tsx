import { useMutation } from '@tanstack/react-query';
import React, { useState } from 'react'
import axios from '../api/axios';
import { useLocation, useNavigate } from 'react-router-dom';
import { AuthResponseType } from '../types/api';
import useAuth from '../hooks/UseAuth';

const LoginPage = () => {

  const {setUser, setAccessToken} = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || '/';

  type LoginType = {
    email: string,
    password: string
  }

  const loginMutation = useMutation({
    mutationFn: (credentials : LoginType) => {
      return axios.post('/auth/authenticate', credentials, {
        withCredentials: true
      })

    },
    onSuccess: (res) => {
      const data: AuthResponseType = res.data;
      setAccessToken(data.accessToken)
      setUser({
        id: data.id,
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        roles: data.roles,
        profileImg: data.profileImg
      })

      navigate(from, {replace: true})
    }
  })


  const handleLogin = e => {
    e.preventDefault();
    loginMutation.mutate({email, password});
  }

  return (
    <div className='flex items-center justify-center h-[calc(100vh-80px)]'>
        <div className='text-center shadow-lg rounded-md py-8 px-10'>
            {/* <h1 className='font-bold text-2xl'>SelfLearnTech</h1> */}
            <img src='./logo.png' className='w-60 mx-auto' />
            <p className='text-gray-500 text-xs mt-2.5'>Welcome back! Please sign in to continue</p>
            <div className='flex justify-center items-center gap-2 my-6'>
              <div className='flex items-center border-gray-300 border-[1px] py-2 px-8 rounded-md hover:cursor-pointer'>
                <a href="" className='w-4 h-4'>
                  <img src='./google-icon.png' className='w-full' />
                </a>
              </div>
              <div className='flex items-center border-gray-300 border-[1px] py-2 px-8 rounded-md hover:cursor-pointer'>
                <a href="" className='w-4 h-4'>
                  <img src='./github-icon.png' className='w-full' />
                </a>
              </div>
            </div>
            {/* <p className='text-gray-500 text-xs my-4'>or</p> */}
            <div className='flex items-center my-4'>
              <hr className='flex-grow border-t border-gray-300' />
              <span className='mx-2 text-gray-500 text-xs'>or</span>
              <hr className='flex-grow border-t border-gray-300' />
            </div>
            <form onSubmit={handleLogin}>
              <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" id='email' required name='email' placeholder='Enter your email' className='border-gray-300 border-[1px] focus:border-2 focus:shadow-sm rounded-md w-full outline-none text-xs py-1 px-2 mb-3' />
              <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" id='password' name='password' required placeholder='Enter your password' className='border-gray-300 border-[1px] focus:border-2 focus:shadow-sm rounded-md w-full outline-none text-xs py-1 px-2' />
              <button className='bg-gray-800 w-full rounded-md text-white text-xs py-1 my-4'>Login</button>
            </form>
            <p className='text-xs'><span className='text-gray-500'>Don't have an account?</span> <button className='font-bold'>Sign up</button></p>

        </div>
    </div>
  )
}

export default LoginPage