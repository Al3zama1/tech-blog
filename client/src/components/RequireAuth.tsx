import useAuth from '@/hooks/UseAuth'
import React, { ReactElement } from 'react'
import { Navigate, Outlet, useLocation } from 'react-router-dom'

type Props = {
    allowedRoles: String[],
}

const RequireAuth = ({ allowedRoles } : Props) => {
    const { user } = useAuth();
    const location = useLocation();

  return (
    user?.roles.find(role => allowedRoles?.includes(role))
    ? <Outlet />
    : user !== undefined
    ? <Navigate to='/unauthorized' state={{ from: location }} replace />
    : <Navigate to="/login" state={{ from: location }} replace />
  )
}

export default RequireAuth