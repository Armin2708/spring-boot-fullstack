import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react'
import CardWithImage from "./components/Card";
import CreateCustomerDrawerForm from "./components/CreateCustomerDrawerForm.jsx";
import {errorNotification} from "./services/notification.js";

const App = () =>{

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchCustomers = () =>{
        setLoading(true);
        getCustomers().then(res =>{
            setCustomers(res.data)
        }).catch(err =>{
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() => {
            setLoading(false)
        })
    }

    useEffect(() => {
            fetchCustomers()
        },[])



    if(loading){
        return(
        <SidebarWithHeader>
            <Spinner
                thickness='4px'
                speed='0.65s'
                emptyColor='gray.200'
                color='blue.500'
                size='xl'
            />
        </SidebarWithHeader>
        )
    }

    if(err){
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawerForm
                    fetchCustomers={fetchCustomers}
                ></CreateCustomerDrawerForm>
                <Text>Ooops there was an error</Text>
            </SidebarWithHeader>
        )
    }


    if(customers.length<=0){
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawerForm
                    fetchCustomers={fetchCustomers}
                    ></CreateCustomerDrawerForm>
                <Text>No Customers Available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawerForm
                fetchCustomers={fetchCustomers}
                />
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer,index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...customer}
                            imageNumber={index}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>

    )
}

export default App;