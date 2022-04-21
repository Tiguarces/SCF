import { UniversalResponse } from "./UniversalResponse";

export interface RefreshTokenResponse extends UniversalResponse{
  accessToken: string,
  refreshToken: string,
  username: string;
}
