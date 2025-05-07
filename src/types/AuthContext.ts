import { UserAuth } from "./UserAuth";

export type AuthContextType = {
  user: UserAuth | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  logout: () => void;
  checkAuth: () => void;
};
