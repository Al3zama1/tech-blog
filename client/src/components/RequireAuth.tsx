import useAuth from '@/hooks/UseAuth'
import React, { ReactElement } from 'react'
import { Navigate, useLocation } from 'react-router-dom'

type Props = {
    allowedRoles: String[],
    children: ReactElement
}

const RequireAuth = ({ allowedRoles, children } : Props) => {
    const { user } = useAuth();
    const location = useLocation();

  return (
    user?.roles.find(role => allowedRoles?.includes(role))
    ? children
    : user !== null
    ? <Navigate to='/unauthorized' state={{ from: location }} replace />
    : <Navigate to="/login" state={{ from: location }} replace />
  )
}

export default RequireAuth