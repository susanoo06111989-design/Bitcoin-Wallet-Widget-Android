# Bitcoin Wallet Widget Android

Read-only Bitcoin mainnet address balance home-screen widget.

## Build
GitHub Actions > Build APK > Run workflow, or push to main. Download `bitcoin-wallet-widget-debug` from workflow artifacts.

## Setup
Install APK, open app, paste a public Bitcoin address, save it, and add the widget from Android home-screen widgets. The wallet address is stored locally, never committed to this repository. No private keys or seed phrases are required.

Balance data: mempool.space API; exchange rates: CoinGecko public API. These public services receive the queried address or price request respectively. Updates are best-effort and subject to Android background limits. No funds can be spent by this app.
