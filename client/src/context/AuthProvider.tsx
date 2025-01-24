import React, { createContext, ReactNode, useState } from 'react'
import { UserType } from '../types/model'

type UserContextType = {
    isLoaded: boolean
    user: UserType | null
    accessToken: string | null,
    setIsLoaded: (loaded: boolean) => void
    setUser: (user: UserType) => void
    setAccessToken: (token: string) => void
}

const UserContext = createContext<UserContextType | null> (null);

export const UserProvider = ({children} : {children: ReactNode}) => {
    const [user, setUser] = useState<UserType | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [isLoaded, setIsLoaded] = useState(false);

    return (
        <UserContext.Provider value={{user, accessToken, isLoaded, setUser, setAccessToken, setIsLoaded}}>
            {children}
        </UserContext.Provider>
    )

}

export default UserContext;