// import { AuthResponseType } from '@/types/api'
import axios from '../api/axios'
import useAuth from './UseAuth';

const userRefreshToken = () => {
    const { setUser, setAccessToken } = useAuth()

    const refresh = async () => {
        const response = await axios.post('/auth/refresh', {}, 
            {
                withCredentials: true
            }
        );

        const data = await response.data;

        setUser({
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            profileImg: data.profileImg,
            roles: data.roles
        })
        setAccessToken(data.accessToken)


        // return data.accessToken;
    }

    return refresh;
}

export default userRefreshToken