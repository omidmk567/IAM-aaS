import {useKeycloak} from "@react-keycloak/web";

export default function Home() {
    const {keycloak} = useKeycloak()

    return (
        <div>
            <h1>Home</h1>
            <h3>Hello <i><b>{keycloak?.tokenParsed?.preferred_username}</b></i></h3>
            {
                keycloak?.authenticated ?
                    <button onClick={() => keycloak.logout()}>Logout</button> :
                    <button onClick={() => keycloak.login()}>Login</button>
            }
            <p>This is the home page.</p>
        </div>
    )
}