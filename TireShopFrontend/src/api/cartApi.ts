import axiosInstance from './axiosInstance';

export interface CartItem {
    id: number;
    productId: number;
    productName: string;
    quantity: number;
    pricePerItem: number;
    totalPrice: number;
    imageUrl?: string;
}

export interface CartSummary {
    items: CartItem[];
    total: number;
}

export const getCartWithImages = async (): Promise<CartItem[]> => {
    const { data: cartItems } = await axiosInstance.get<CartItem[]>('/api/cart');

    const withImages = await Promise.all(
        cartItems.map(async (item) => {
            try {
                const imageRes = await axiosInstance.get(`/api/image/products/${item.productId}`);
                const imageUrl = imageRes.data[0]?.url || '';
                return { ...item, imageUrl };
            } catch {
                return { ...item, imageUrl: '' };
            }
        })
    );

    return withImages;
};

export const updateCartItem = async (id: number, quantity: number): Promise<string> => {
    const res = await axiosInstance.patch(`/api/cart/${id}`, { quantity });
    return res.data;
};

export const deleteCartItem = async (id: number): Promise<string> => {
    const res = await axiosInstance.delete(`/api/cart/${id}`);
    return res.data;
};

export const clearCart = async (): Promise<string> => {
    const res = await axiosInstance.delete(`/api/cart`);
    return res.data;
};

export const addToCart = async (productId: number, quantity: number): Promise<string> => {
    const res = await axiosInstance.post('/api/cart', { productId, quantity });
    return res.data;
};