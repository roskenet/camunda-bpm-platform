package org.camunda.bpm.qa.upgrade.scenarios780;

import java.util.List;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.qa.upgrade.Origin;

import org.camunda.bpm.qa.upgrade.ScenarioUnderTest;
import org.camunda.bpm.qa.upgrade.json.beans.Customer;
import org.camunda.bpm.qa.upgrade.json.beans.ObjectList;
import org.camunda.bpm.qa.upgrade.json.beans.Order;

import org.camunda.bpm.qa.upgrade.json.beans.OrderDetails;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ScenarioUnderTest("CreateProcessInstanceWithJsonVariablesScenario")
@Origin("7.8.0")
public class CreateProcessInstanceWithJsonVariablesTest {

  @Rule
  public ProcessEngineRule engineRule = new ProcessEngineRule("camunda.json.cfg.xml");

  @ScenarioUnderTest("initProcessInstance.1")
  @Test
  public void testCreateProcessInstanceWithVariable() {
    // then
    ProcessInstance processInstance = engineRule.getRuntimeService().createProcessInstanceQuery().processInstanceBusinessKey("processWithJsonVariables").singleResult();
    List<VariableInstance> variables = engineRule.getRuntimeService().createVariableInstanceQuery().processInstanceIdIn(processInstance.getId()).list();
    assertEquals(4, variables.size());

    final Object objectVariable = engineRule.getRuntimeService().getVariable(processInstance.getId(), "objectVariable");
    assertObjectVariable(objectVariable);

    final Object plainTypeArrayVariable = engineRule.getRuntimeService().getVariable(processInstance.getId(), "plainTypeArrayVariable");
    assertPlainTypeArrayVariable(plainTypeArrayVariable);

    final Object objectArrayVariable = engineRule.getRuntimeService().getVariable(processInstance.getId(), "objectArrayVariable");
    assertObjectArrayVariable(objectArrayVariable);

    final Object notGenericObjectListVariable = engineRule.getRuntimeService().getVariable(processInstance.getId(), "notGenericObjectListVariable");
    assertNotGenericObjectListVariable(notGenericObjectListVariable);
  }

  private void assertNotGenericObjectListVariable(Object notGenericObjectListVariable) {
    assertTrue(notGenericObjectListVariable instanceof ObjectList);
    final ObjectList list = (ObjectList) notGenericObjectListVariable;
    assertEquals(2, list.size());
    //TODO
  }

  private void assertObjectArrayVariable(Object objectArrayVariable) {
    assertTrue(objectArrayVariable instanceof Object[]);
    Object[] array = (Object[]) objectArrayVariable;
    assertEquals(2, array.length);
    //TODO
  }

  public void assertObjectVariable(Object objectVariable) {
    assertTrue(objectVariable instanceof Order);
    Order order = (Order)objectVariable;
    //check couple of fields
    assertEquals(1234567890987654321L, order.getId());
    assertEquals("order1", order.getOrder());
    assertTrue(order.isActive());
  }

  public void assertPlainTypeArrayVariable(Object plainTypeArrayVariable) {
    assertTrue(plainTypeArrayVariable instanceof int[]);
    int[] array = (int[])plainTypeArrayVariable;
    assertEquals(2, array.length);
    //TODO
  }

}
