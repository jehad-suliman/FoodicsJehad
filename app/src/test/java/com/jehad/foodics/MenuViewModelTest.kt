package com.jehad.foodics


import com.jehad.foodics.data.repository.OrderRepository
import com.jehad.foodics.domain.model.Category
import com.jehad.foodics.domain.model.OrderItem
import com.jehad.foodics.domain.model.Product
import com.jehad.foodics.domain.usecase.GetCategoriesUseCase
import com.jehad.foodics.domain.usecase.GetProductsUseCase
import com.jehad.foodics.ui.screens.menu.MenuViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MenuViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var orderRepository: OrderRepository
    private lateinit var viewModel: MenuViewModel

    // Sample data
    private val categories = listOf(
        Category("cat1", "Category 1"),
        Category("cat2", "Category 2")
    )

    private val products = listOf(
        Product(
            "prod1",
            "Product 1",
            price = 10.0,
            description = "Description 1",
            category = categories[0]
        ),
        Product(
            "prod2",
            "Product 2",
            price = 15.0,
            description = "Description 2",
            category = categories[0]
        ),
        Product(
            "prod3",
            "Product 3",
            price = 20.0,
            description = "Description 3",
            category = categories[1]
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getCategoriesUseCase = mockk()

        coEvery { getCategoriesUseCase.refreshCategories() } returns Unit
        every { getCategoriesUseCase.getCategories() } returns flowOf(categories)
        every { getCategoriesUseCase.invoke() } returns flowOf(categories)


        getProductsUseCase = mockk {
            coEvery { refreshProducts() } returns Unit
            every { getProductsByCategory("cat1") } returns flowOf(products.filter { it.category?.id == "cat1" })
            every { getProductsByCategory("cat2") } returns flowOf(products.filter { it.category?.id == "cat2" })
        }

        orderRepository = mockk {
            every { getTotalItems() } returns flowOf(0)
            every { getTotalPrice() } returns flowOf(0.0)
            every { getOrderItems() } returns flowOf(emptyList())
            coEvery { addOrUpdateOrderItem(any()) } returns Unit
        }

        viewModel = MenuViewModel(getCategoriesUseCase, getProductsUseCase, orderRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `adding product to order should update quantities and maintain selected category`() =
        runTest {
            // Print the actual products list to see what we're starting with
            println("Products: ${products.map { it.id to it.category?.id }}")

            // Print the filtered products to ensure our filter works
            println("Products for cat2: ${products.filter { it.category?.id == "cat2" }.map { it.id }}")

            // Setup initial state with category "cat2" selected
            viewModel.selectCategory("cat2")
            testDispatcher.scheduler.advanceUntilIdle()

            // Print the state to see what's actually in there
            val initialState = viewModel.state.value
            println("State after selection: category=${initialState.selectedCategoryId}, products=${initialState.products.map { it.id }}")

            assertEquals("cat2", initialState.selectedCategoryId)
            assertEquals(1, initialState.products.size)  // This is likely failing
            assertEquals("prod3", initialState.products[0].id)
            assertEquals(0, initialState.products[0].quantity)

            // Setup order repository to return an updated order item
            val updatedProduct = products[2].copy(quantity = 1)
            val orderItem = OrderItem(updatedProduct, 1)
            every { orderRepository.getOrderItems() } returns flowOf(listOf(orderItem))

            // Add product to order
            viewModel.addProductToOrder(products[2])
            testDispatcher.scheduler.advanceUntilIdle()

            // Verify product was added to order
            coVerify { orderRepository.addOrUpdateOrderItem(products[2]) }

            // Verify state after adding product
            val updatedState = viewModel.state.value
            assertEquals("cat2", updatedState.selectedCategoryId) // Category should remain the same
            assertEquals(1, updatedState.products.size)
            assertEquals("prod3", updatedState.products[0].id)
            assertEquals(1, updatedState.products[0].quantity) // Quantity should be updated
        }

    @Test
    fun `search should filter products across all categories`() = runTest {
        // Setup
        every { getProductsUseCase.getAllProducts() } returns flowOf(products)

        // Initial state
        viewModel.selectCategory("cat1")
        testDispatcher.scheduler.advanceUntilIdle()

        // Perform search
        viewModel.updateSearchQuery("Product 3")
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify search results
        val searchState = viewModel.state.value
        assertTrue(searchState.isSearching)
        assertEquals(1, searchState.products.size)
        assertEquals("prod3", searchState.products[0].id)

        // Clear search
        viewModel.updateSearchQuery("")
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify we're back to category view
        val finalState = viewModel.state.value
        assertEquals(false, finalState.isSearching)
        assertEquals("cat1", finalState.selectedCategoryId)
        assertEquals(2, finalState.products.size) // Should show products from cat1
    }
}