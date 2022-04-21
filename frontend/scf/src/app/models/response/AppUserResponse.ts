import { AppUser } from "../AppUser";
import { UniversalResponse } from "./UniversalResponse";

export interface AppUserResponse extends UniversalResponse {
  user: AppUser;
}
