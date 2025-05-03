export type ProductType = 'TIRE' | 'ACCESSORY' | 'RIM' | 'ALL';
export type AccessoryType = 'SENSOR' | 'BOLT' | 'CHAINS' | 'COVERS' | 'JACK' | 'TOOLS';
export interface Accessory {
    id: number;
    name: string;
    price: number;
    stock: number;
    description: string;
    productType: ProductType;
    accessoryType: AccessoryType;
    imageUrls?: string[];
}

