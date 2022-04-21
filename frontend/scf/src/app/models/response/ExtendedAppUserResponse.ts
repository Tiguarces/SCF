import { ExtendedAppUser } from "../ExtendedAppUser";
import { UniversalResponse } from "./UniversalResponse";

export interface ExtendedAppUserResponse extends UniversalResponse{
  appUser: ExtendedAppUser;
}
