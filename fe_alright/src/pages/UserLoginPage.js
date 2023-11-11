import {useState } from 'react';
// import axios from 'axios';
import { useHistory} from 'react-router';
import NavBar from '../components/NavBar';
import LoginForm from '../components/LoginForm';

const UserLoginPage = () => {
    // const history = useHistory();
    // const [pwd,setPwd] = useState('');

    // const onSubmit = () => {
    //     const data = {
    //       password: pwd
    //     };
      
    //     axios.post('http://172.20.37.28:4000/admin/login', JSON.stringify(data), {
    //       headers: {
    //         'Content-Type': 'application/json'
    //       }
    //     })
    //     .then(res=> {
    //         const data = res.data
    //         console.log(data.status)
    //         history.push('/login');
    //       })
    //     .catch(error => {
    //         console.error(error);
    //       });
    // };
      
    return (
        <div>
        <NavBar />
        <div style = {{padding: '100px'}}>
            <div className="container d-flex align-items-center justify-content-center vh-50">
                <div className="d-flex flex-column align-items-center">
                    <h1>일반 사용자 로그인</h1>
                    <LoginForm />
                </div>
            </div>
        </div>
        </div>
    );
}
export default UserLoginPage;