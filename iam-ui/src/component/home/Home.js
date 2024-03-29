import {useKeycloak} from "@react-keycloak/web";

export default function Home() {
    const {keycloak} = useKeycloak()

    return (
        <div>
            This is the home page. You can login or logout using the buttons below.
            <br/>
            {
                keycloak?.authenticated ?
                    <button onClick={() => keycloak.logout()}>Logout</button> :
                    <button onClick={() => keycloak.login()}>Login</button>
            }
            <br/>
            <br/>
            After logging in, you can navigate to the dashboard by clicking the button below.
            <br/>
            <button onClick={() => window.location.href = '/dash'}>Go to Dashboard</button>
        </div>
    )
}