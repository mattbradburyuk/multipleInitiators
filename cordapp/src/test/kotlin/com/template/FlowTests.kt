package com.template

import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.testing.node.MockNetwork
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowTests {

    private val network = MockNetwork(listOf("com.template"))

    val aX500 = CordaX500Name("PartyA","London","GB")
    val bX500 = CordaX500Name("PartyB","New York","US")
    val cX500 = CordaX500Name("PartyC","Paris","FR")

    private val a = network.createPartyNode(aX500)
    private val b = network.createNode(bX500)
    private val c = network.createNode(cX500)

    init {
        listOf(a, b, c).forEach {
            it.registerInitiatedFlow(Responder_A::class.java)
            it.registerInitiatedFlow(Responder_B::class.java)
        }
    }

    @Before
    fun setup() = network.runNetwork()

    @After
    fun tearDown() = network.stopNodes()

    @Test
    fun `partyA test with flow inheritance`() {

        val flow = Initiator_A()

        val future = a.startFlow(flow)

        network.runNetwork()

        future.getOrThrow()

    }


    @Test
    fun `partyB test with flow inheritance`() {

        val flow = Initiator_B()

        val future = b.startFlow(flow)

        network.runNetwork()

        future.getOrThrow()

    }

    @Test
    fun `partyA test with subflows`() {

        val flow = Initiator_A2()

        val future = a.startFlow(flow)

        network.runNetwork()

        future.getOrThrow()

    }


    @Test
    fun `partyB test with subflows`() {

        val flow = Initiator_B2()

        val future = b.startFlow(flow)

        network.runNetwork()

        future.getOrThrow()

    }

}