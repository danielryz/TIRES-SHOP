export interface Image {
  id: number;
  url: string;
  productId: number;
  publicId: string;
}

export interface CreateImageRequest {
  url: string;
  productId: number;
  publicId: string;
}

export interface AddImageRequest {
  url: string;
  productId: number;
  publicId: string;
}
