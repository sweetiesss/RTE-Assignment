import { User } from "./User";

export type AuthContextType = {
  user: User | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  logout: () => void;
  checkAuth: () => void;
};
