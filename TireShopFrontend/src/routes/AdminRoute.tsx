import { Navigate, Outlet } from 'react-router-dom';
import { isAdmin } from '../utils/authUtils';

const AdminRoute = () => {
    const token = localStorage.getItem('token');

    if (!token || !isAdmin()) {
        return <Navigate to="/admin-login" replace />;
    }

    return <Outlet />;
};

export default AdminRoute;
