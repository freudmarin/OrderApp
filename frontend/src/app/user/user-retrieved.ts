import {Role} from "./role";

export class UserRetrieved {
  username: String;
  role : Role;
  authenticationToken: string;
}
