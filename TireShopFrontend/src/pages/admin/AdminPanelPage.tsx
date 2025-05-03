import LogoutButton from '../../components/button/LogoutButton';

function AdminPanelPage() {
    return (
        <div style={{ padding: '30px', textAlign: 'center' }}>
            <h1>Admin Management Panel</h1>
            <p>Here you will be able to manage products, orders, and users.</p>
            <LogoutButton />
        </div>
    );
}

export default AdminPanelPage;
