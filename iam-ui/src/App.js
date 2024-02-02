import './App.css';
import {ReactKeycloakProvider} from '@react-keycloak/web';
import Keycloak from 'keycloak-js';
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import PrivateRoute from "./component/global/PrivateRoute";
import Home from "./component/home/Home";
import Dashboard from "./component/home/Dashboard";

function App() {
    const keycloak = new Keycloak({
        url: 'http://localhost:8080',
        realm: "IAM-aaS",
        clientId: "iam-customers"
    })
    const initOptions = {pkceMethod: 'S256'}
    const handleOnEvent = async (event, error) => {
        console.log(event, error)
    }

    const loadingComponent = (
        <div>
            <h1>Loading...</h1>
        </div>
    )

    return (
        <ReactKeycloakProvider
            authClient={keycloak}
            initOptions={initOptions}
            LoadingComponent={loadingComponent}
            onEvent={(event, error) => handleOnEvent(event, error)}
        >
            <Router>
                <Routes>
                    <Route path='/' element={<Home/>}/>
                    <Route path='/dash' element={<PrivateRoute><Dashboard/></PrivateRoute>}/>
                </Routes>
            </Router>
        </ReactKeycloakProvider>
    )
}

export default App;
