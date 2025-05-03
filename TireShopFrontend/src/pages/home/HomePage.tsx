import { Link } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
    return (

        <div className="home-wrapper">
            <div className="home-main-box">
                <h1 className="home-title">Witamy w TireShop!</h1>
                <p className="home-subtitle">Wybierz kategorię produktów, aby rozpocząć zakupy.</p>
            </div>
            <div style={{ height: '2000px' }} />
        </div>
    );
}

export default HomePage;
