import { createContext, useContext, useState, useEffect } from 'react';
import { getCartWithImages } from '../api/cartApi';

interface CartContextProps {
    count: number;
    refreshCart: () => void;
}

export const CartContext = createContext<CartContextProps>({
    count: 0,
    refreshCart: () => {},
});

export const useCart = () => useContext(CartContext);

export const CartProvider = ({ children }: { children: React.ReactNode }) => {
    const [count, setCount] = useState(0);

    const refreshCart = async () => {
        try {
            const items = await getCartWithImages();
            const total = items.reduce((acc, item) => acc + item.quantity, 0);
            setCount(total);
        } catch {
            setCount(0);
        }
    };

    useEffect(() => {
        refreshCart();
    }, []);

    return (
        <CartContext.Provider value={{ count, refreshCart }}>
            {children}
        </CartContext.Provider>
    );
};

