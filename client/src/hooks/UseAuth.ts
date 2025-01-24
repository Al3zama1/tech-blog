import { useContext } from "react";
import UserContext from "../context/AuthProvider";

const useAuth = () => {
    const userContext = useContext(UserContext)

    if (!userContext) throw new Error("UserContext has to be used within <UserContext.Provider>")

        return userContext;

}

export default useAuth;