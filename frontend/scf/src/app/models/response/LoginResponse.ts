import { UniversalResponse } from "./UniversalResponse";

export interface LoginResponse extends UniversalResponse{
  accessToken: string,
  refreshToken: string,
  username: string
}
