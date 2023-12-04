import {useKeycloak} from "@react-keycloak/web";

export default function PrivateRoute({ children }) {
  const { keycloak } = useKeycloak()

  const Login = () => {
    keycloak.login()
  }

  return keycloak.authenticated ? children : <Login />
}