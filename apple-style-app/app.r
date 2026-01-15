

library(shiny)
library(shinydashboard)
library(plotly)
library(DT)
library(dplyr)
library(readr)
library(shinycssloaders)

ui <- dashboardPage(
  skin = "blue",
  dashboardHeader(title = "Analyse Apple-Style"),
  dashboardSidebar(
    sidebarMenu(
      menuItem("Import", tabName = "import", icon = icon("upload")),
      menuItem("Exploration", tabName = "explore", icon = icon("chart-bar")),
      menuItem("Résumé", tabName = "summary", icon = icon("table"))
    )
  ),
  dashboardBody(
    # 📎 Lien vers le fichier CSS Apple-style
    tags$head(
      tags$link(rel = "stylesheet", type = "text/css", href = "style.css")
    ),
    tabItems(
      tabItem(tabName = "import",
              fluidRow(
                box(title = "Charger un fichier CSV", width = 6,
                    fileInput("file", "Choisir un fichier CSV",
                              accept = c(".csv")),
                    checkboxInput("header", "Entête ?", TRUE),
                    radioButtons("sep", "Séparateur",
                                 choices = c("Virgule" = ",",
                                             "Point-virgule" = ";",
                                             "Tabulation" = "\t"),
                                 selected = ",")
                )
              )
      ),
      tabItem(tabName = "explore",
              fluidRow(
                box(title = "Choix des variables", width = 4,
                    uiOutput("var_select_x"),
                    uiOutput("var_select_y"),
                    checkboxInput("add_smooth", "Ajouter une courbe de tendance", TRUE)
                ),
                box(title = "Graphique interactif", width = 8,
                    withSpinner(plotlyOutput("scatter_plot"), type = 6)
                )
              )
      ),
      tabItem(tabName = "summary",
              fluidRow(
                box(title = "Résumé statistique", width = 6,
                    verbatimTextOutput("summary_stats")
                ),
                box(title = "Aperçu des données", width = 6,
                    withSpinner(DTOutput("data_table"))
                )
              )
      )
    )
  )
)

server <- function(input, output, session) {
  
  data <- reactive({
    req(input$file)
    tryCatch({
      read_csv(input$file$datapath,
               col_names = input$header,
               delim = input$sep)
    }, error = function(e) {
      showNotification("Erreur de lecture du fichier.", type = "error")
      NULL
    })
  })
  
  output$var_select_x <- renderUI({
    req(data())
    selectInput("var_x", "Variable X", choices = names(data()))
  })
  
  output$var_select_y <- renderUI({
    req(data())
    selectInput("var_y", "Variable Y", choices = names(data()))
  })
  
  output$scatter_plot <- renderPlotly({
    req(data(), input$var_x, input$var_y)
    
    p <- ggplot(data(), aes_string(x = input$var_x, y = input$var_y)) +
      geom_point(color = "#007aff") +
      theme_minimal()
    
    if (input$add_smooth) {
      p <- p + geom_smooth(method = "lm", se = FALSE, color = "#ff2d55")
    }
    
    ggplotly(p)
  })
  
  output$summary_stats <- renderPrint({
    req(data())
    summary(data())
  })
  
  output$data_table <- renderDT({
    req(data())
    datatable(data(), options = list(pageLength = 10))
  })
}

shinyApp(ui, server)
