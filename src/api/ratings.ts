// src/api/ratings.ts
import { fetchWithAuth } from "./config";

export const ratings = {
  getByProductId: (productId: string) =>
    fetchWithAuth(`/products/${productId}/ratings`),

  create: (ratingData: {
    productId: number;
    userId?: number;
    point: number;
    description: string;
  }) =>
    fetchWithAuth("/ratings", {
      method: "POST",
      body: JSON.stringify(ratingData),
    }),

  update: (
    ratingId: number,
    ratingData: { point: number; description: string }
  ) =>
    fetchWithAuth(`/ratings/${ratingId}`, {
      method: "PUT",
      body: JSON.stringify(ratingData),
    }),

  delete: (ratingId: number) =>
    fetchWithAuth(`/ratings/${ratingId}`, {
      method: "DELETE",
    }),
};
