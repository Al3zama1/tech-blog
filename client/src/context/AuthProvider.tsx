import React, { createContext, ReactNode, useEffect, useState } from 'react'
import { UserType } from '../types/model'
import { useMutation } from '@tanstack/react-query'
import axios, { axiosPrivate } from '@/api/axios'
import { AuthResponseType } from '@/types/api'

type UserContextType = {
    // isLoading: boolean
    user: UserType | null
    accessToken: string | null,
    // setIsLoading: (loaded: boolean) => void
    setUser: (user: UserType) => void
    setAccessToken: (token: string) => void
}

const UserContext = createContext<UserContextType | null> (null);

export const UserProvider = ({children} : {children: ReactNode}) => {
    const [user, setUser] = useState<UserType | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [isLoadingUser, setIsLoadingUser] = useState(true);

    const {mutate: refreshUserAccess, isPending} = useMutation({
        mutationFn: () => {
            return axiosPrivate.post('/auth/refresh-access', {});
        },
        onSuccess: (res) => {
            const data: AuthResponseType = res.data;
            setUser({
                firstName: data.firstName,
                lastName: data.lastName,
                email: data.email,
                roles: data.roles,
                profileImg: data.profileImg
            })
            setAccessToken(data.accessToken)
            setIsLoadingUser(false)
        },
        onError: (error) => {
            setIsLoadingUser(false)
        }
    })

    useEffect(() => {
        refreshUserAccess();    
    }, [])

    if (isLoadingUser) return;


    return (
        <UserContext.Provider value={{user, accessToken, setUser, setAccessToken}}>
            {children}
        </UserContext.Provider>
    )

}

export default UserContext;