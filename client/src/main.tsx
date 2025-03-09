import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {
  createBrowserRouter,
  RouterProvider,
} from 'react-router-dom'
import HomePage from './routes/HomePage.tsx'
import PostListPage from './routes/PostListPage.tsx'
import LoginPage from './routes/LoginPage.tsx'
import RegisterPage from './routes/RegisterPage.tsx'
import SinglePostPage from './routes/SinglePostPage.tsx'
import MainLayout from './layouts/MainLayout.tsx'
import { UserProvider } from './context/AuthProvider.tsx'
import {
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query'
import { ToastContainer } from 'react-toastify'
import RequireAuth from './components/RequireAuth.tsx'
import Unauthorized from './components/Unauthorized.tsx'
import WritePage from './routes/WritePage.tsx'

const queryClient = new QueryClient();

const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      {
        path: "/",
        element: <HomePage />
      },
      {
        path: "/posts",
        element: <PostListPage />
      },
      {
        path: "/:slug",
        element: <SinglePostPage />
      },
      {
        path: "/draft/:draftId",
        element: <RequireAuth allowedRoles={["ADMIN", "EDITOR"]} children={<WritePage />} />
      },
      {
        path: "/login",
        element: <LoginPage />
      },
      {
        path: "/register",
        element: <RegisterPage />
      },
      {
        path: "/unauthorized",
        element: <Unauthorized />
      }
    ]
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <RouterProvider router={router} />
        <ToastContainer position='bottom-right' />
      </UserProvider>
    </QueryClientProvider>
  </StrictMode>,
)
