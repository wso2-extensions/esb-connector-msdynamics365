package org.wso2.carbon.connector.integration.test.msdynamics365;
/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/


import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Sample integration test
 */
public class MsDynamics365IntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        String connectorName = System.getProperty("connector_name") + "-connector-" +
                System.getProperty("connector_version") + ".zip";
        init(connectorName);
        getApiConfigProperties();
        getAccessToken();

        esbRequestHeadersMap.put("Content-Type", "application/json");
        esbRequestHeadersMap.put("Accept", "application/json");
    }

    private void getAccessToken() throws IOException, JSONException {

        Map<String, String> apiTokenRequestHeadersMap = new HashMap<String, String>();
        Map<String, String> apiParametersMap = new HashMap<String, String>();
        apiTokenRequestHeadersMap.put("Content-Type", "application/x-www-form-urlencoded");
        RestResponse<JSONObject> apiTokenRestResponse =
                sendJsonRestRequest(connectorProperties.getProperty("loginEndpoint") +
                                "/common/oauth2/token", "POST",
                        apiTokenRequestHeadersMap, "tokenRequest.txt", apiParametersMap);

        String accessToken = apiTokenRestResponse.getBody().get("access_token").toString();
        connectorProperties.put("accessToken",accessToken);
    }

    /**
     * Positive test case for createEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 1,
            description = "msdynamics365 {createEntity} integration test with mandatory parameters.")
    public void testCreateEntityWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:createEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createEntity_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for associateEntitiesOnCreate method with mandatory parameters.
     */
    @Test(enabled = true, priority = 2,
            description = "msdynamics365 {associateEntitiesOnCreate} integration test with mandatory parameters.")
    public void testAssociateEntitiesOnCreateWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:createEntity");

        RestResponse<JSONObject> esbRestContactResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createContact.json");

        String entityId = esbRestContactResponse.getHeadersMap().get("OData-EntityId").toString();
        String contactId = entityId.substring(entityId.indexOf('(') + 1, entityId.indexOf(')'));

        connectorProperties.setProperty("bodyContentForAssociateEntityOnCreate",
                connectorProperties.getProperty("bodyContentForAssociateEntityOnCreate").replace("<id>", contactId));

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "associateEntitiesOnCreate_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for createEntityWithDataReturned method with mandatory parameters.
     */
    @Test(enabled = true, priority = 3,
            description = "msdynamics365 {createEntityWithDataReturned} integration test with mandatory parameters.")
    public void testCreateEntityWithDataReturnedWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:createEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createEntityWithDataReturned_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
    }

    /**
     * Negative test case for createEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 4,
            description = "msdynamics365 {createEntity} integration test with negative case.")
    public void testCreateEntityNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:createEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "createEntity_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "Resource not found for the segment 'test'.");
    }

    /**
     * Positive test case for deleteEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 5,
            description = "msdynamics365 {deleteEntity} integration test with mandatory parameters.")
    public void testDeleteEntityWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:deleteEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteEntity_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for deleteSinglePropertyValue method with mandatory parameters.
     */
    @Test(enabled = true, priority = 6,
            description = "msdynamics365 {deleteSinglePropertyValue} integration test with mandatory parameters.")
    public void testDeleteSinglePropertyValueWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:deleteSinglePropertyValue");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteSinglePropertyValue_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for deleteEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 7,
            description = "msdynamics365 {deleteEntity} integration test with negative case.")
    public void testDeleteEntityNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:deleteEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "deleteEntity_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "Account With Id = f9d858aa-864e-e711-80f8-c4346bad2694 Does Not Exist");
    }

    /**
     * Positive test case for updateEntity method with mandatory parameters.
     */
    @Test(enabled = true, priority = 8,
            description = "msdynamics365 {updateEntity} integration test with mandatory parameters.")
    public void testUpdateEntityWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:updateEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "updateEntity_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for associateEntitiesOnUpdate method with mandatory parameters.
     */
    @Test(enabled = true, priority = 9,
            description = "msdynamics365 {associateEntitiesOnUpdate} integration test with mandatory parameters.")
    public void testAssociateEntitiesOnUpdateWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:createEntity");

        RestResponse<JSONObject> esbRestAccountResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createAccount.json");

        String entityId = esbRestAccountResponse.getHeadersMap().get("OData-EntityId").toString();
        String accountId = entityId.substring(entityId.indexOf('(') + 1, entityId.indexOf(')'));
        connectorProperties.setProperty("primaryKey", accountId);
        connectorProperties.setProperty("primaryKeyForRetrieveNavigationProperties", accountId);

        String content = connectorProperties.getProperty("bodyContentForAssociateOnUpdate").replace("<id>", accountId);
        content = content.replace("<resource>", connectorProperties.getProperty("resource"));
        connectorProperties.setProperty("bodyContentForAssociateOnUpdate", content);

        content = connectorProperties.getProperty("contentForAssociateWithSingleValued").replace("<id>", accountId);
        content = content.replace("<resource>", connectorProperties.getProperty("resource"));
        connectorProperties.setProperty("contentForAssociateWithSingleValued", content);

        esbRequestHeadersMap.put("Action", "urn:updateEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "associateEntitiesOnUpdate_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }
    /**
     * Positive test case for updateEntityWithDataReturned method with mandatory parameters.
     */
    @Test(enabled = true, priority = 10,
            description = "msdynamics365 {updateEntityWithDataReturned} integration test with mandatory parameters.")
    public void testUpdateEntityWithDataReturnedWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:updateEntityWithDataReturnedOrSinglePropertyValue");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "updateEntityWithDataReturned_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for updateEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 11,
            description = "msdynamics365 {updateEntity} integration test with negative case.")
    public void testUpdateEntityNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:updateEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "updateEntity_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "Resource not found for the segment 'test'.");
    }

    /**
     * Positive test case for updateSinglePropertyValue method with mandatory parameters.
     */
    @Test(enabled = true, priority = 12,
            description = "msdynamics365 {updateSinglePropertyValue} integration test with mandatory parameters.")
    public void testUpdateSinglePropertyValueWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:updateEntityWithDataReturnedOrSinglePropertyValue");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "updateSinglePropertyValue_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for updateSinglePropertyValue method with mandatory parameters.
     */
    @Test(enabled = true, priority = 13,
            description = "msdynamics365 {updateSinglePropertyValue} integration test with negative case.")
    public void testUpdateSinglePropertyValueNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:updateEntityWithDataReturnedOrSinglePropertyValue");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "updateSinglePropertyValue_negative.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);

    }

    /**
     * Positive test case for upsertEntity (preventCreateInUpsert) method with mandatory parameters.
     */
    @Test(enabled = true, priority = 14,
            description = "msdynamics365 {upsertEntity} integration test with mandatory parameters.")
    public void testUpsertEntityWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:upsertEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "upsertEntity_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for upsertEntity (preventCreateInUpsert) method with mandatory parameters.
     */
    @Test(enabled = true, priority = 15,
            description = "msdynamics365 {upsertEntity} integration test with negative case.")
    public void testUpsertEntityNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:upsertEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "upsertEntity_negative.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);
    }

    /**
     * Positive test case for upsertEntity (preventUpdateInUpsert) method with mandatory parameters.
     * If the entity isn’t found,  will get a normal response with status 204 (No Content).
     */
    @Test(enabled = true, priority = 16,
            description = "msdynamics365 {upsertEntity} integration test with mandatory parameters.")
    public void testPreventUpdateInUpsertWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:upsertEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "preventUpdateInUpsert_negative.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for upsertEntity (preventUpdateInUpsert) method with mandatory parameters.
     * When the entity is found, will get the (Precondition Failed) with status 412 .
     */
    @Test(enabled = true, priority = 17,
            description = "msdynamics365 {upsertEntity} integration test with negative case.")
    public void testPreventUpdateInUpsertNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:upsertEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "preventUpdateInUpsert_mandatory.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "A record with matching key values already exists.");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 412);
    }

    /**
     * Positive test case for retrieveEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 18,
            description = "msdynamics365 {retrieveEntity} integration test with mandatory parameters.")
    public void testRetrieveEntityWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveEntity_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for retrieveEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 19,
            description = "msdynamics365 {retrieveEntity} integration test with negative case.")
    public void testRetrieveEntityNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveEntity_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "account With Id = f9d858aa-864e-e711-80f8-c4346bad2694 Does Not Exist");
    }

    /**
     * Positive test case for retrieveSinglePropertyOfAnEntity method with mandatory parameters.
     */
    @Test(enabled = true, priority = 20,
            description = "msdynamics365 {retrieveSingleProperty} integration test with mandatory parameters.")
    public void testRetrieveSinglePropertyWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveSingleProperty");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveSingleProperty_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for retrieveSinglePropertyOfAnEntity method with mandatory parameters.
     */
    @Test(enabled = true, priority = 21,
            description = "msdynamics365 {retrieveSinglePropertyOfAnEntity} integration test with negative case.")
    public void testRetrieveSinglePropertyValueNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveSingleProperty");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveSingleProperty_negative.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);

    }

    /**
     * Positive test case for retrieveSpecificPropertiesOfAnEntity method with mandatory parameters.
     */
    @Test(enabled = true, priority = 22,
            description = "msdynamics365 {retrieveSpecificProperties} integration test with mandatory parameters.")
    public void testRetrieveSpecificPropertiesWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveSpecificProperties");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveSpecificProperties_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for retrieveByExpandingNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 23,
            description = "msdynamics365 {retrieveByExpandingNavigationProperties} " +
                    "integration test with mandatory parameters.")
    public void testRetrieveByExpandingNavigationPropertiesWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveByExpandingNavigationProperties");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveByExpandingNavigationProperties_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for retrieveSingleValuedNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 24,
            description = "msdynamics365 {retrieveSingleValuedNavigationProperties} " +
                    "integration test with mandatory parameters.")
    public void testRetrieveSingleValuedNavigationPropertiesWithMandatoryParameters()
            throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveNavigationPropertyValues");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveSingleValuedNavigationProperties_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for retrieveSingleValuedNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 25,
            description = "msdynamics365 {retrieveSingleValuedNavigationProperties} integration test with negative case.")
    public void testRetrieveSingleValuedNavigationPropertiesNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveNavigationPropertyValues");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveSingleValuedNavigationProperties_negative.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 404);

    }

    /**
     * Positive test case for retrieveCollectionValuedNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 26,
            description = "msdynamics365 {retrieveCollectionValuedNavigationProperties} " +
                    "integration test with mandatory parameters.")
    public void testRetrieveCollectionValuedNavigationPropertiesWithMandatoryParameters()
            throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveNavigationPropertyValues");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveCollectionValuedNavigationProperties_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Negative test case for retrieveCollectionValuedNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 27,
            description = "msdynamics365 {retrieveCollectionValuedNavigationProperties} integration test " +
                    "with negative case.")
    public void testRetrieveCollectionValuedNavigationPropertiesNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:retrieveNavigationPropertyValues");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "retrieveCollectionValuedNavigationProperties_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "The URI segment '$ref' is invalid after the segment 'test'.");
    }

    /**
     * Positive test case for associateAnEntityWithCollectionValuedNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 28,
            description = "msdynamics365 {associateAnExistingEntityWithCollectionValuedNavigationProperties } " +
                    "integration test with mandatory parameters.")
    public void testAssociateAnEntityToCollectionValuedPropertyWithMandatoryParameters()
            throws IOException, JSONException {

        esbRequestHeadersMap.put("Action", "urn:createEntity");

        RestResponse<JSONObject> esbRestAccountResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "createOpportunity.json");

        String entityId = esbRestAccountResponse.getHeadersMap().get("OData-EntityId").toString();
        String opportunityId = entityId.substring(entityId.indexOf('(') + 1, entityId.indexOf(')'));

        connectorProperties.setProperty("primaryKeyValueForAssociation", opportunityId);

        String content = connectorProperties.getProperty("bodyContentForAssociation").replace("<id>", opportunityId);
        content = content.replace("<resource>", connectorProperties.getProperty("resource"));
        connectorProperties.setProperty("bodyContentForAssociation", content);

        content = connectorProperties.getProperty("queryParamForRemoveReference").replace("<id>", opportunityId);
        content = content.replace("<resource>", connectorProperties.getProperty("resource"));
        connectorProperties.setProperty("queryParamForRemoveReference", content);

        esbRequestHeadersMap.put("Action", "urn:associateAnExistingEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "associateWithCollectionValuedProperty_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for associateAnEntityWithSingleValuedNavigationProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 29,
            description = "msdynamics365 {associateAnEntityWithSingleValuedNavigationProperties} " +
                    "integration test with mandatory parameters.")
    public void testChangeReferenceInSingleValuedPropertiesWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:associateAnExistingEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "associateWithSingleValuedProperty_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for associateAnEntities method with mandatory parameters.
     */
    @Test(enabled = true, priority = 30,
            description = "msdynamics365 {associateAnEntities} integration test " +
                    "with negative case.")
    public void testChangeReferenceInSingleValuedNavigationPropertiesNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:associateAnExistingEntity");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "associateAnEntity_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "The URI segment '$ref' is invalid after the segment 'customerid_account'.");
    }

    /**
     * Positive test case for removeEntityReferenceUsingSingleValuedProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 31,
            description = "msdynamics365 {removeEntityReferenceUsingSingleValuedProperties} " +
                    "integration test with mandatory parameters.")
    public void testRemoveReferenceUsingSingleValuedPropertyWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:removeEntityReferenceForSingleValuedProperty");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "removeEntityReferenceUsingSingleValuedProperties_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for removeEntityReferenceUsingCollectionValuedProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 32,
            description = "msdynamics365 {removeEntityReferenceUsingCollectionValuedProperties} " +
                    "integration test with mandatory parameters.")
    public void testRemoveReferenceUsingCollectionValuedPropertiesWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:removeEntityReferenceForCollectionValuedProperty");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "removeEntityReferenceUsingCollectionValuedProperties_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Positive test case for removeEntityReferenceUsingCollectionValuedProperties method with mandatory parameters.
     */
    @Test(enabled = true, priority = 34,
            description = "msdynamics365 {removeEntityReferenceUsingSingleQueryParams} " +
                    "integration test with mandatory parameters.")
    public void testRemoveReferenceUsingQueryParamsWithMandatoryParameters() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:removeEntityReferenceForCollectionValuedProperty");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "removeEntityReferenceUsingQueryParams_mandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
    }

    /**
     * Negative test case for removeEntityReference method with mandatory parameters.
     */
    @Test(enabled = true, priority = 35,
            description = "msdynamics365 {removeEntityReference} integration test " + "with negative case.")
    public void testRemoveReferenceNegativeCase() throws IOException, JSONException {
        esbRequestHeadersMap.put("Action", "urn:removeEntityReferenceForSingleValuedProperty");

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
                        "removeEntityReference_negative.json");
        Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("message"),
                "The URI segment '$ref' is invalid after the segment 'customerid_account'.");
    }
}