import {useKeycloak} from "@react-keycloak/web";

export default function Dashboard() {
    const {keycloak} = useKeycloak()

    return (
        <div>
            <h1>Home</h1>
            <h3>Hello <i><b>{keycloak?.tokenParsed?.preferred_username}</b></i></h3>
            <h3>Your token is:</h3>
            <p>{keycloak?.token}</p>
            <button onClick={() => navigator.clipboard.writeText(keycloak?.token)}>Copy to clipboard</button>
            <br/>
            {
                keycloak?.authenticated ?
                    <button onClick={() => keycloak.logout()}>Logout</button> :
                    <button onClick={() => keycloak.login()}>Login</button>
            }

        </div>
    )
}