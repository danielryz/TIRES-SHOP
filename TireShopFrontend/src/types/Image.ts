export interface Image {
  id: number;
  url: string;
  productId: number;
}

export interface CreateImageRequest {
  url: string;
  productId: number;
}

export interface AddImageRequest {
  url: string;
}
