import { useEffect } from "react";
import { axiosPrivate } from "../api/axios";
import useAuth from "./UseAuth";
import userRefreshToken from "./useRefreshToken";

const useAxiosPrivate = () => {

    const refresh = userRefreshToken();
    const { accessToken } = useAuth()

    useEffect(() => {
        // Initial request
        const requestInterceptor = axiosPrivate.interceptors.request.use(
            config => {
                if (!config.headers['Authorization']) {
                    config.headers['Authorization'] = `Bearer ${accessToken}`
                }

                return config;
            }, (error) => Promise.reject(error)
        );

        // Retry request
        const responseInterceptor = axiosPrivate.interceptors.response.use(
            response => response,
            async (error) => {
                const prevRequest = error?.config;
                if (error?.response?.status === 401 && !prevRequest?.sent) {
                    prevRequest.sent = true;
                    const newAccessToken = await refresh();
                    prevRequest.headers['Authorization'] = `Bearer ${newAccessToken}`
                    return axiosPrivate(prevRequest);
                }
                return Promise.reject(error);
            }
        );

        return () => {
            axiosPrivate.interceptors.response.eject(requestInterceptor);
            axiosPrivate.interceptors.response.eject(responseInterceptor);
        }
    }, [accessToken, refresh])

    return axiosPrivate;
}

export default useAxiosPrivate;
