// src/api/index.ts
import { auth } from "./auth";
import { products } from "./products";
import { categories } from "./categories";
import { ratings } from "./ratings";
import { users } from "./users";

export const api = {
  auth,
  products,
  categories,
  ratings,
  users,
};
